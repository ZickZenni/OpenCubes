package net.opencubes.client.level.chunk;

import net.opencubes.block.Block;
import net.opencubes.block.BlockRegistry;
import net.opencubes.block.BlockSide;
import net.opencubes.client.level.postprocess.LevelAmbientOcclusion;
import net.opencubes.client.vertex.Face;
import net.opencubes.client.vertex.Model;
import net.opencubes.client.vertex.util.CubeMesh;
import net.opencubes.client.vertex.util.MeshBuilder;
import net.opencubes.world.level.ChunkPos;
import net.opencubes.world.level.Level;
import net.opencubes.world.level.chunk.ChunkBlock;
import net.opencubes.world.level.chunk.LevelChunk;
import net.opencubes.world.level.lighting.LevelLightingEngine;
import net.opencubes.world.physics.Vec3;

/*
 TODO: Rewrite to support Ambient Occlusion
 */
public class ChunkMesh {
    private final LevelChunk chunk;

    private Model opaqueModel;
    private Model transparentModel;

    public ChunkMesh(LevelChunk chunk) {
        this.chunk = chunk;
    }

    public void computeMeshes() {
        MeshBuilder opaqueMeshBuilder = new MeshBuilder();
        MeshBuilder transparentMeshBuilder = new MeshBuilder();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 255; y >= 0; y--) {
                    if (chunk.getBlockAt(x, y, z) == null)
                        continue;
                    ChunkBlock chunkBlock = chunk.getBlockAt(x, y, z);
                    Block block = BlockRegistry.getBlock(chunkBlock.getBlockName());
                    generateMesh(block.isTransparent() ? transparentMeshBuilder : opaqueMeshBuilder, x, y, z);
                }
            }
        }

        destroyMeshes();

        Vec3 modelPosition = new Vec3(chunk.getChunkPos().x() * 16 + 0.5f, 0, chunk.getChunkPos().z() * 16 + 0.5f);
        if (opaqueMeshBuilder.getFaces().size() > 0) {
            this.opaqueModel = new Model(opaqueMeshBuilder.build(), 1, modelPosition);
        }
        if (transparentMeshBuilder.getFaces().size() > 0) {
            this.transparentModel = new Model(transparentMeshBuilder.build(), 1, modelPosition);
        }
    }

    public void destroyMeshes() {
        if (opaqueModel != null && opaqueModel.getMesh() != null) {
            opaqueModel.getMesh().cleanup();
            opaqueModel = null;
        }
        if (transparentModel != null && transparentModel.getMesh() != null) {
            transparentModel.getMesh().cleanup();
            transparentModel = null;
        }
    }

    private void generateMesh(MeshBuilder meshBuilder, int x, int y, int z) {
        Level level = chunk.getLevel();
        int chunkX = chunk.getChunkPos().x();
        int chunkZ = chunk.getChunkPos().z();

        if (chunk.getBlockAt(x, y, z) == null)
            return;

        ChunkBlock chunkBlock = chunk.getBlockAt(x, y, z);
        Block currentBlock = BlockRegistry.getBlock(chunkBlock.getBlockName());

        if (x > 0) {
            Face face = generateFace(meshBuilder, BlockSide.LEFT, x, y, z, -1, 0, 0);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            LevelChunk neighbor = level.getChunk(new ChunkPos(chunkX - 1, chunkZ));
            if (neighbor != null) {
                ChunkBlock neighborBlock = neighbor.getBlockAt(15, y, z);
                if (neighborBlock == null) {
                    Face face = CubeMesh.getLeftFace(chunkBlock.getBlockName());
                    face.setColors(
                            computeColors(
                                    LevelLightingEngine.computeLightLevel(chunk.getLightLevel(x, y, z)) - LevelLightingEngine.LIGHT_LEVEL * 3,
                                    currentBlock.getTint(BlockSide.LEFT), LevelAmbientOcclusion.compute(chunk, BlockSide.LEFT, x, y, z)));
                    meshBuilder.addFace(face, x, y, z);
                }
            }
        }
        if (x < 15) {
            Face face = generateFace(meshBuilder, BlockSide.RIGHT, x, y, z, 1, 0, 0);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            LevelChunk neighbor = level.getChunk(new ChunkPos(chunkX + 1, chunkZ));
            if (neighbor != null) {
                ChunkBlock neighborBlock = neighbor.getBlockAt(0, y, z);
                if (neighborBlock == null) {
                    Face face = CubeMesh.getRightFace(chunkBlock.getBlockName());
                    face.setColors(
                            computeColors(
                                    LevelLightingEngine.computeLightLevel(chunk.getLightLevel(x, y, z)) - LevelLightingEngine.LIGHT_LEVEL * 3,
                                    currentBlock.getTint(BlockSide.RIGHT), LevelAmbientOcclusion.compute(chunk, BlockSide.RIGHT, x, y, z)));
                    meshBuilder.addFace(face, x, y, z);
                }
            }
        }
        if (y > 0) {
            Face face = generateFace(meshBuilder, BlockSide.BOTTOM, x, y, z, 0, -1, 0);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            Face face = CubeMesh.getBottomFace(chunkBlock.getBlockName());
            face.setColors(
                    computeColors(
                            LevelLightingEngine.computeLightLevel(chunk.getLightLevel(x, y, z)) - LevelLightingEngine.LIGHT_LEVEL * 5,
                            currentBlock.getTint(BlockSide.BOTTOM), LevelAmbientOcclusion.compute(chunk, BlockSide.BOTTOM, x, y, z)));
            meshBuilder.addFace(face, x, y, z);
        }
        if (y < 254) {
            Face face = generateFace(meshBuilder, BlockSide.TOP, x, y, z, 0, 1, 0);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        }
        if (z > 0) {
            Face face = generateFace(meshBuilder, BlockSide.BACK, x, y, z, 0, 0, -1);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            LevelChunk neighbor = level.getChunk(new ChunkPos(chunkX, chunkZ - 1));
            if (neighbor != null) {
                ChunkBlock neighborBlock = neighbor.getBlockAt(x, y, 15);
                if (neighborBlock == null) {
                    Face face = CubeMesh.getBackFace(chunkBlock.getBlockName());
                    face.setColors(
                            computeColors(
                                    LevelLightingEngine.computeLightLevel(chunk.getLightLevel(x, y, z)) - LevelLightingEngine.LIGHT_LEVEL * 3,
                                    currentBlock.getTint(BlockSide.BACK), LevelAmbientOcclusion.compute(chunk, BlockSide.BACK, x, y, z)));
                    meshBuilder.addFace(face, x, y, z);
                }
            }
        }
        if (z < 15) {
            Face face = generateFace(meshBuilder, BlockSide.FRONT, x, y, z, 0, 0, 1);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            LevelChunk neighbor = level.getChunk(new ChunkPos(chunkX, chunkZ + 1));
            if (neighbor != null) {
                ChunkBlock neighborBlock = neighbor.getBlockAt(x, y, 0);
                if (neighborBlock == null) {
                    Face face = CubeMesh.getFrontFace(chunkBlock.getBlockName());
                    face.setColors(
                            computeColors(
                                    LevelLightingEngine.computeLightLevel(chunk.getLightLevel(x, y, z)) - LevelLightingEngine.LIGHT_LEVEL * 3,
                                    currentBlock.getTint(BlockSide.FRONT), LevelAmbientOcclusion.compute(chunk, BlockSide.FRONT, x, y, z)));
                    meshBuilder.addFace(face, x, y, z);
                }
            }
        }
    }

    private Face generateFace(MeshBuilder meshBuilder, BlockSide side, int x, int y, int z, int offsetX, int offsetY, int offsetZ) {
        if (meshBuilder == null || chunk == null)
            return null;
        ChunkBlock chunkBlock = chunk.getBlockAt(x, y, z);
        ChunkBlock offsetBlock = chunk.getBlockAt(x + offsetX, y + offsetY, z + offsetZ);
        if (offsetBlock != null || chunkBlock == null) {
            return null;
        }
        String blockName = chunkBlock.getBlockName();
        float lightLevel = LevelLightingEngine.computeLightLevel(chunk.getLightLevel(x + offsetX, y + offsetY, z + offsetZ));

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
        float[] ao = LevelAmbientOcclusion.compute(chunk, side, x, y, z);
        face.setColors(computeColors(lightLevel, BlockRegistry.getBlock(blockName).getTint(side), ao));
        return face;
    }

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

    public Model getOpaqueModel() {
        return opaqueModel;
    }

    public Model getTransparentModel() {
        return transparentModel;
    }

    public LevelChunk getChunk() {
        return chunk;
    }
}
