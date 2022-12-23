package eu.zickzenni.opencubes.world.chunk;

import eu.zickzenni.opencubes.block.Block;
import eu.zickzenni.opencubes.block.BlockSide;
import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.OpenCubes;
import eu.zickzenni.opencubes.client.mesh.CubeMesh;
import eu.zickzenni.opencubes.client.mesh.Face;
import eu.zickzenni.opencubes.client.mesh.MeshBuilder;
import eu.zickzenni.opencubes.world.Dimension;
import org.joml.Vector3f;

import java.util.ArrayList;

public class ChunkMeshSystem {
    private static final int MAX_CHUNKS_PER_TICK = 6;

    private static final ArrayList<Chunk> chunks = new ArrayList<>();

    public static boolean push(Chunk chunk) {
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
            return true;
        }
        return false;
    }

    public static void update() {
        if (OpenCubes.getInstance().getWorld() == null) {
            chunks.clear();
            return;
        }

        if (chunks.size() != 0) {
            if (chunks.size() < MAX_CHUNKS_PER_TICK) {
                Chunk[] chks = new Chunk[chunks.size()];
                for (int i = 0; i < chunks.size(); i++) {
                    chks[i] = chunks.get(i);
                }
                chunks.clear();
                generate(chks);
            } else {
                Chunk[] chks = new Chunk[MAX_CHUNKS_PER_TICK];
                for (int i = 0; i < MAX_CHUNKS_PER_TICK; i++) {
                    if (i >= chunks.size()) {
                        break;
                    }
                    chks[i] = chunks.get(i);
                    chunks.remove(chunks.get(i));
                    generate(chks);
                }
            }
        }

        // Multi-threading takes too much memory
        // (With this)

        /*for (int i = 0; i < threads.size(); i++) {
            if (!threads.get(i).isAlive()) {
                threads.remove(threads.get(i));
            }
        }

        if (threads.size() < MAX_THREADS) {
            if (chunks.size() < MAX_CHUNKS_PER_THREAD) {
                Chunk[] chks = new Chunk[chunks.size()];
                for (int i = 0; i < chunks.size(); i++) {
                    chks[i] = chunks.get(i);
                }
                chunks.clear();
                Thread thread = new Thread(() -> {
                    generate(chks);
                });
                thread.start();
                threads.add(thread);

                for (Chunk chk : chks) {
                    System.out.println(chk.getX() + "," + chk.getZ());
                }
            } else {
                Chunk[] chks = new Chunk[MAX_CHUNKS_PER_THREAD];
                for (int i = 0; i < MAX_CHUNKS_PER_THREAD; i++) {
                    if (i >= chunks.size()) {
                        break;
                    }
                    chks[i] = chunks.get(i);
                    chunks.remove(chunks.get(i));
                }
                Thread thread = new Thread(() -> {
                    generate(chks);
                });

                thread.start();
                threads.add(thread);
            }
        }
        for (int i = 0; i < threads.size(); i++) {
            if (!threads.get(i).isAlive()) {
                threads.remove(threads.get(i));
            }
        }*/
    }

    private static void generate(Chunk[] chunks) {
        for (Chunk chunk : chunks) {
            if (chunk == null || chunk.isDestroyed())
                continue;

            MeshBuilder meshBuilder = new MeshBuilder();
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 255; y >= 0; y--) {
                        generateMesh(meshBuilder, chunk, x, y, z);
                    }
                }
            }
            chunk.builder = meshBuilder;
            chunk.meshGenerated = true;
            chunk.buildMesh();
        }
    }

    private static void generateMesh(MeshBuilder meshBuilder, Chunk chunk, int x, int y, int z) {
        Dimension dimension = chunk.getDimension();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        if (!chunk.doesBlockExist(x, y, z))
            return;

        ChunkBlock chunkBlock = chunk.getBlockAt(x, y, z);
        byte blockId = chunkBlock.getBlockId();
        Block currentBlock = Blocks.getBlock(blockId);

        if (x > 0) {
            Face face = generateFace(meshBuilder, chunk, BlockSide.LEFT, x,y,z,-1, 0, 0);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            Chunk neighbor = dimension.getChunk(new ChunkPosition(chunkX - 1, chunkZ));
            if (neighbor != null) {
                if (neighbor.getBlockId(15, y, z) > 0) {
                    Block block = Blocks.getBlock(neighbor.getBlockAt(15, y, z).getBlockId());
                    if (chunk.canRender(block)) {
                        Face face = CubeMesh.getLeftFace(blockId);
                        face.setColors(calculateColors(neighbor.calculateLightLevel(15, y, z) - Chunk.LIGHT_LEVEL * 3, currentBlock.getTint(BlockSide.LEFT)));
                        meshBuilder.addFace(face, x, y, z);
                    }
                } else {
                    Face face = CubeMesh.getLeftFace(blockId);
                    face.setColors(calculateColors(neighbor.calculateLightLevel(15, y, z) - Chunk.LIGHT_LEVEL * 3, currentBlock.getTint(BlockSide.LEFT)));
                    meshBuilder.addFace(face, x, y, z);
                }
            }
        }
        if (x < 15) {
            Face face = generateFace(meshBuilder, chunk, BlockSide.RIGHT, x,y,z,1, 0, 0);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            Chunk neighbor = dimension.getChunk(new ChunkPosition(chunkX + 1, chunkZ));
            if (neighbor != null) {
                if (neighbor.getBlockId(0, y, z) > 0) {
                    Block block = Blocks.getBlock(neighbor.getBlockAt(0, y, z).getBlockId());
                    if (chunk.canRender(block)) {
                        Face face = CubeMesh.getRightFace(blockId);
                        face.setColors(calculateColors(neighbor.calculateLightLevel(0, y, z) - Chunk.LIGHT_LEVEL * 3, currentBlock.getTint(BlockSide.RIGHT)));
                        meshBuilder.addFace(face, x, y, z);
                    }
                } else {
                    Face face = CubeMesh.getRightFace(blockId);
                    face.setColors(calculateColors(neighbor.calculateLightLevel(0, y, z) - Chunk.LIGHT_LEVEL * 3, currentBlock.getTint(BlockSide.RIGHT)));
                    meshBuilder.addFace(face, x, y, z);
                }
            }
        }
        if (y > 0) {
            Face face = generateFace(meshBuilder, chunk, BlockSide.BOTTOM, x,y,z, 0, -1, 0);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            Face face = CubeMesh.getBottomFace(blockId);
            face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z) - Chunk.LIGHT_LEVEL * 5, currentBlock.getTint(BlockSide.BOTTOM)));
            meshBuilder.addFace(face, x, y, z);
        }
        if (y < 254) {
            Face face = generateFace(meshBuilder, chunk, BlockSide.TOP,  x,y,z,0, 1, 0);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        }
        if (z > 0) {
            Face face = generateFace(meshBuilder, chunk, BlockSide.BACK, x,y,z, 0, 0, -1);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            Chunk neighbor = dimension.getChunk(new ChunkPosition(chunkX, chunkZ - 1));
            if (neighbor != null) {
                if (neighbor.getBlockId(x, y, 15) > 0) {
                    Block block = Blocks.getBlock(neighbor.getBlockAt(x, y, 15).getBlockId());
                    if (chunk.canRender(block)) {
                        Face face = CubeMesh.getBackFace(blockId);
                        face.setColors(calculateColors(neighbor.calculateLightLevel(x, y, 15) - Chunk.LIGHT_LEVEL * 3, currentBlock.getTint(BlockSide.BACK)));
                        meshBuilder.addFace(face, x, y, z);
                    }
                } else {
                    Face face = CubeMesh.getBackFace(blockId);
                    face.setColors(calculateColors(neighbor.calculateLightLevel(x, y, 15) - Chunk.LIGHT_LEVEL * 3, currentBlock.getTint(BlockSide.BACK)));
                    meshBuilder.addFace(face, x, y, z);
                }
            }
        }
        if (z < 15) {
            Face face = generateFace(meshBuilder, chunk, BlockSide.FRONT, x,y,z, 0, 0, 1);
            if (face != null) {
                meshBuilder.addFace(face, x, y, z);
            }
        } else {
            Chunk neighbor = dimension.getChunk(new ChunkPosition(chunkX, chunkZ + 1));
            if (neighbor != null) {
                if (neighbor.getBlockId(x, y, 0) != 0) {
                    Block block = Blocks.getBlock(neighbor.getBlockAt(x, y, 0).getBlockId());
                    if (chunk.canRender(block)) {
                        Face face = CubeMesh.getFrontFace(blockId);
                        face.setColors(calculateColors(neighbor.calculateLightLevel(x, y, 0) - Chunk.LIGHT_LEVEL * 3, currentBlock.getTint(BlockSide.FRONT)));
                        meshBuilder.addFace(face, x, y, z);
                    }
                } else {
                    Face face = CubeMesh.getFrontFace(blockId);
                    face.setColors(calculateColors(neighbor.calculateLightLevel(x, y, 0) - Chunk.LIGHT_LEVEL * 3, currentBlock.getTint(BlockSide.FRONT)));
                    meshBuilder.addFace(face, x, y, z);
                }
            }
        }
    }

    private static Face generateFace(MeshBuilder meshBuilder, Chunk chunk, BlockSide side, int x, int y, int z, int offsetX, int offsetY, int offsetZ) {
        if (meshBuilder == null || chunk == null || chunk.getBlockId(x + offsetX, y + offsetY, z + offsetZ) == -1)
            return null;
        if (!Chunk.inChunk(x + offsetX, y + offsetY, z + offsetZ))
            return null;
        byte blockId = chunk.getBlockId(x, y, z);
        if (blockId == 0)
            return null;

        if (chunk.getBlockId(x + offsetX, y + offsetY, z + offsetZ) != 0) {
            ChunkBlock nChunkBlock = chunk.getBlockAt(x + offsetX, y + offsetY, z + offsetZ);
            Block nBlock = Blocks.getBlock(nChunkBlock.getBlockId());
            if (chunk.canRender(nBlock)) {
                float lightLevel = chunk.calculateLightLevel(x + offsetX, y + offsetY, z + offsetZ);
                Face face = null;
                switch (side) {
                    case FRONT -> {
                        face = CubeMesh.getFrontFace(blockId);
                        lightLevel -= Chunk.LIGHT_LEVEL * 3;
                    }
                    case BACK -> {
                        face = CubeMesh.getBackFace(blockId);
                        lightLevel -= Chunk.LIGHT_LEVEL * 3;
                    }
                    case LEFT -> {
                        face = CubeMesh.getLeftFace(blockId);
                        lightLevel -= Chunk.LIGHT_LEVEL * 3;
                    }
                    case RIGHT -> {
                        face = CubeMesh.getRightFace(blockId);
                        lightLevel -= Chunk.LIGHT_LEVEL * 3;
                    }
                    case TOP -> face = CubeMesh.getTopFace(blockId);
                    case BOTTOM -> {
                        face = CubeMesh.getBottomFace(blockId);
                        lightLevel -= Chunk.LIGHT_LEVEL * 5;
                    }
                }
                face.setColors(calculateColors(lightLevel, Blocks.getBlock(blockId).getTint(side)));
                return face;
            }
        } else {
            float lightLevel = chunk.calculateLightLevel(x + offsetX, y + offsetY, z + offsetZ);
            Face face = null;
            switch (side) {
                case FRONT -> {
                    face = CubeMesh.getFrontFace(blockId);
                    lightLevel -= Chunk.LIGHT_LEVEL * 3;
                }
                case BACK -> {
                    face = CubeMesh.getBackFace(blockId);
                    lightLevel -= Chunk.LIGHT_LEVEL * 3;
                }
                case LEFT -> {
                    face = CubeMesh.getLeftFace(blockId);
                    lightLevel -= Chunk.LIGHT_LEVEL * 3;
                }
                case RIGHT -> {
                    face = CubeMesh.getRightFace(blockId);
                    lightLevel -= Chunk.LIGHT_LEVEL * 3;
                }
                case TOP -> face = CubeMesh.getTopFace(blockId);
                case BOTTOM -> {
                    face = CubeMesh.getBottomFace(blockId);
                    lightLevel -= Chunk.LIGHT_LEVEL * 5;
                }
            }
            face.setColors(calculateColors(lightLevel, Blocks.getBlock(blockId).getTint(side)));
            return face;
        }
        return null;
    }

    private static float[] calculateColors(float lightLevel, Vector3f tint) {
        float colorR = lightLevel * tint.x;
        float colorG = lightLevel * tint.y;
        float colorB = lightLevel * tint.z;
        return new float[]{
                colorR, colorG, colorB, 1,
                colorR, colorG, colorB, 1,
                colorR, colorG, colorB, 1,
                colorR, colorG, colorB, 1,
        };
    }

    public static int getQueuedChunks() {
        return chunks.size();
    }
}