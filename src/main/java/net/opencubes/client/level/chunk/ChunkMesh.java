package net.opencubes.client.level.chunk;

import net.opencubes.block.Block;
import net.opencubes.block.BlockRegistry;
import net.opencubes.block.BlockSide;
import net.opencubes.client.level.postprocess.LevelAmbientOcclusion;
import net.opencubes.client.vertex.Face;
import net.opencubes.client.vertex.Model;
import net.opencubes.client.vertex.util.CubeMesh;
import net.opencubes.client.vertex.util.MeshBuilder;
import net.opencubes.world.level.Level;
import net.opencubes.world.level.chunk.ChunkBlock;
import net.opencubes.world.level.chunk.LevelChunk;
import net.opencubes.world.level.lighting.LevelLightingEngine;
import net.opencubes.world.physics.Vec3;

import java.util.HashMap;
import java.util.Map;

public class ChunkMesh {
    private final LevelChunk chunk;

    private HashMap<String, Model> opaqueModels = new HashMap<>();
    private HashMap<String, Model> transparentModels = new HashMap<>();

    public ChunkMesh(LevelChunk chunk) {
        this.chunk = chunk;
    }

    public void computeMeshes() {
        HashMap<String, MeshBuilder> opaqueBuilders = new HashMap<>();
        HashMap<String, MeshBuilder> transparentBuilders = new HashMap<>();

        opaqueBuilders.put("default", new MeshBuilder());
        transparentBuilders.put("default", new MeshBuilder());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 255; y >= 0; y--) {
                    if (chunk.getBlockAt(x, y, z) == null)
                        continue;
                    ChunkBlock chunkBlock = chunk.getBlockAt(x, y, z);
                    Block block = BlockRegistry.getBlock(chunkBlock.getBlockName());
                    MeshBuilder builder = new MeshBuilder();
                    if (block.isTransparent()) {
                        if (transparentBuilders.containsKey(block.shader())) {
                            builder = transparentBuilders.get(block.shader());
                        }
                        generateMesh(builder, x, y, z);
                        transparentBuilders.put(block.shader(), builder);
                    } else {
                        if (opaqueBuilders.containsKey(block.shader())) {
                            builder = opaqueBuilders.get(block.shader());
                        }
                        generateMesh(builder, x, y, z);
                        opaqueBuilders.put(block.shader(), builder);
                    }
                }
            }
        }
        destroyMeshes();

        Vec3 modelPosition = new Vec3(chunk.getChunkPos().x() * 16 + 0.5f, 0, chunk.getChunkPos().z() * 16 + 0.5f);
        for (Map.Entry<String, MeshBuilder> entry : opaqueBuilders.entrySet()) {
            opaqueModels.put(entry.getKey(), new Model(entry.getValue().build(), 1, modelPosition));
        }
        for (Map.Entry<String, MeshBuilder> entry : transparentBuilders.entrySet()) {
            transparentModels.put(entry.getKey(), new Model(entry.getValue().build(), 1, modelPosition));
        }
    }

    public void destroyMeshes() {
        for (Map.Entry<String, Model> entry : opaqueModels.entrySet()) {
            entry.getValue().getMesh().cleanup();
        }
        for (Map.Entry<String, Model> entry : transparentModels.entrySet()) {
            entry.getValue().getMesh().cleanup();
        }
        opaqueModels.clear();
        transparentModels.clear();
    }

    private void generateMesh(MeshBuilder meshBuilder, int x, int y, int z) {
        if (chunk.getBlockAt(x, y, z) == null)
            return;

        generateFace(meshBuilder, BlockSide.RIGHT, x, y, z, 1, 0, 0);
        generateFace(meshBuilder, BlockSide.LEFT, x, y, z, -1, 0, 0);
        generateFace(meshBuilder, BlockSide.TOP, x, y, z, 0, 1, 0);
        generateFace(meshBuilder, BlockSide.BOTTOM, x, y, z, 0, -1, 0);
        generateFace(meshBuilder, BlockSide.FRONT, x, y, z, 0, 0, 1);
        generateFace(meshBuilder, BlockSide.BACK, x, y, z, 0, 0, -1);
    }

    private void generateFace(MeshBuilder meshBuilder, BlockSide side, int x, int y, int z, int offsetX, int offsetY, int offsetZ) {
        if (meshBuilder == null || chunk == null)
            return;

        Level level = chunk.getLevel();
        ChunkBlock block = chunk.getBlockAt(x,y,z);
        if (block == null) {
            return;
        }
        String blockName = block.getBlockName();
        ChunkBlock offsetBlock = chunk.getBlockAt(x + offsetX,y + offsetY,z + offsetZ);
        if (!chunk.inChunk(x + offsetX,y + offsetY,z + offsetZ)) {
            offsetBlock = level.getBlock(x + chunk.getChunkPos().x() * 16 + offsetX, y + offsetY, z + chunk.getChunkPos().z() * 16 + offsetZ);
        }
        if (offsetBlock != null) {
            Block bl = BlockRegistry.getBlock(blockName);
            Block oBlock = BlockRegistry.getBlock(offsetBlock.getBlockName());
            if (bl.isTransparent()) {
                if (oBlock.isTransparent()) {
                    return;
                }
            } else {
                if (!oBlock.isTransparent()) {
                    return;
                }
            }
        }
        float lightLevel = LevelLightingEngine.computeLightLevel(chunk.getLightLevel(x + offsetX, y + offsetY, z + offsetZ));
        if (!chunk.inChunk(x + offsetX,y + offsetY,z + offsetZ)) {
            lightLevel = LevelLightingEngine.computeLightLevel(level.getLightLevel(x + chunk.getChunkPos().x() * 16 + offsetX, y + offsetY, z + chunk.getChunkPos().z() * 16 + offsetZ));
        }

        Face face = null;
        switch (side) {
            case FRONT -> {
                face = CubeMesh.getFrontFace(blockName);
                lightLevel -= LevelLightingEngine.LIGHT_LEVEL * 3;
            }
            case BACK -> {
                face = CubeMesh.getBackFace(blockName);
                lightLevel -= LevelLightingEngine.LIGHT_LEVEL * 3;
            }
            case LEFT -> {
                face = CubeMesh.getLeftFace(blockName);
                lightLevel -= LevelLightingEngine.LIGHT_LEVEL * 3;
            }
            case RIGHT -> {
                face = CubeMesh.getRightFace(blockName);
                lightLevel -= LevelLightingEngine.LIGHT_LEVEL * 3;
            }
            case TOP -> face = CubeMesh.getTopFace(blockName);
            case BOTTOM -> {
                face = CubeMesh.getBottomFace(blockName);
                lightLevel -= LevelLightingEngine.LIGHT_LEVEL * 5;
            }
        }
        if (face != null) {
            float[] ao = LevelAmbientOcclusion.compute(chunk, side, x, y, z);
            face.setColors(computeColors(lightLevel, BlockRegistry.getBlock(blockName).getTint(side), ao));
            meshBuilder.addFace(face, x, y, z);
        }
    }

    /**
     * Compute all colors (light level, tint, ambient occlusion) into one
     */
    public static float[] computeColors(float lightLevel, Vec3 tint, float[] ao) {
        float colorR = lightLevel * tint.x;
        float colorG = lightLevel * tint.y;
        float colorB = lightLevel * tint.z;
        return new float[]{
                colorR * ao[0], colorG * ao[0], colorB * ao[0], 1,
                colorR * ao[1], colorG * ao[1], colorB * ao[1], 1,
                colorR * ao[2], colorG * ao[2], colorB * ao[2], 1,
                colorR * ao[3], colorG * ao[3], colorB * ao[3], 1,
        };
    }

    public HashMap<String, Model> getOpaqueModels() {
        return opaqueModels;
    }

    public HashMap<String, Model> getTransparentModels() {
        return transparentModels;
    }

    public LevelChunk getChunk() {
        return chunk;
    }
}
