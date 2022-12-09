package eu.zickzenni.opencubes.world;

import eu.zickzenni.opencubes.OpenCubes;
import eu.zickzenni.opencubes.block.Block;
import eu.zickzenni.opencubes.block.BlockSide;
import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.engine.mesh.Face;
import eu.zickzenni.opencubes.client.engine.mesh.MeshBuilder;
import eu.zickzenni.opencubes.client.mesh.CubeMesh;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;

public class ChunkMeshSystem {
    private static final int MAX_THREADS = 5;
    private static final int MAX_CHUNKS_PER_THREAD = 3;

    private static final ArrayList<Chunk> chunks = new ArrayList<>();
    private static final ArrayList<Thread> threads = new ArrayList<>();

    public static void push(Chunk chunk) {
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
        }
    }

    public static void update() {
        if (OpenCubes.getInstance().getWorld() == null) {
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    thread.interrupt();
                }
            }
            threads.clear();
            chunks.clear();
            return;
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
        }
    }

    private static void generate(Chunk[] chunks) {
        for (Chunk chunk : chunks) {
            if (chunk == null)
                continue;
            MeshBuilder meshBuilder = new MeshBuilder();

            int chunkX = chunk.getX();
            int chunkZ = chunk.getZ();

            Dimension dimension = chunk.getDimension();
            for (int x = 0; x < 16; x++) {
                for (int y = 255; y >= 0; y--) {
                    for (int z = 0; z < 16; z++) {
                        byte blockId = chunk.getBlockAt(x, y, z);
                        Block currentBlock = Blocks.getBlock(blockId);
                        if (currentBlock.isAir()) {
                            continue;
                        }

                        if (x > 0) {
                            Block block = Blocks.getBlock(chunk.getBlockAt(x - 1, y, z));
                            if (chunk.canRender(block)) {
                                Face face = CubeMesh.getLeftFace(blockId);
                                face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.LEFT)));
                                meshBuilder.addFace(face, x, y, z);
                            }
                        } else {
                            Chunk neighbor = dimension.getChunk(new ChunkPosition(chunkX - 1, chunkZ));
                            if (neighbor != null) {
                                Block block = Blocks.getBlock(neighbor.getBlockAt(new Vector3i(15, y, z)));
                                if (chunk.canRender(block)) {
                                    Face face = CubeMesh.getLeftFace(blockId);
                                    face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.LEFT)));
                                    meshBuilder.addFace(face, x, y, z);
                                }
                            }
                        }
                        if (x < 15) {
                            Block block = Blocks.getBlock(chunk.getBlockAt(x + 1, y, z));
                            if (chunk.canRender(block)) {
                                Face face = CubeMesh.getRightFace(blockId);
                                face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.RIGHT)));
                                meshBuilder.addFace(face, x, y, z);
                            }
                        } else {
                            Chunk neighbor = dimension.getChunk(new ChunkPosition(chunkX + 1, chunkZ));
                            if (neighbor != null) {
                                Block block = Blocks.getBlock(neighbor.getBlockAt(new Vector3i(0, y, z)));
                                if (chunk.canRender(block)) {
                                    Face face = CubeMesh.getRightFace(blockId);
                                    face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.RIGHT)));
                                    meshBuilder.addFace(face, x, y, z);
                                }
                            }
                        }
                        if (y > 0) {
                            Block block = Blocks.getBlock(chunk.getBlockAt(x, y - 1, z));
                            if (chunk.canRender(block)) {
                                Face face = CubeMesh.getBottomFace(blockId);
                                face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.BOTTOM)));
                                meshBuilder.addFace(face, x, y, z);
                            }
                        } else {
                            Face face = CubeMesh.getBottomFace(blockId);
                            face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.BOTTOM)));
                            meshBuilder.addFace(face, x, y, z);
                        }
                        if (y < 254) {
                            Block block = Blocks.getBlock(chunk.getBlockAt(x, y + 1, z));
                            if (chunk.canRender(block)) {
                                Face face = CubeMesh.getTopFace(blockId);
                                face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.TOP)));
                                meshBuilder.addFace(face, x, y, z);
                            }
                        }
                        if (z > 0) {
                            Block block = Blocks.getBlock(chunk.getBlockAt(x, y, z - 1));
                            if (chunk.canRender(block)) {
                                Face face = CubeMesh.getBackFace(blockId);
                                face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.BACK)));
                                meshBuilder.addFace(face, x, y, z);
                            }
                        } else {
                            Chunk neighbor = dimension.getChunk(new ChunkPosition(chunkX, chunkZ - 1));
                            if (neighbor != null) {
                                Block block = Blocks.getBlock(neighbor.getBlockAt(new Vector3i(x, y, 15)));
                                if (chunk.canRender(block)) {
                                    Face face = CubeMesh.getBackFace(blockId);
                                    face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.BACK)));
                                    meshBuilder.addFace(face, x, y, z);
                                }
                            }
                        }
                        if (z < 15) {
                            Block block = Blocks.getBlock(chunk.getBlockAt(x, y, z + 1));
                            if (chunk.canRender(block)) {
                                Face face = CubeMesh.getFrontFace(blockId);
                                face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.FRONT)));
                                meshBuilder.addFace(face, x, y, z);
                            }
                        } else {
                            Chunk neighbor = dimension.getChunk(new ChunkPosition(chunkX, chunkZ + 1));
                            if (neighbor != null) {
                                Block block = Blocks.getBlock(neighbor.getBlockAt(new Vector3i(x, y, 0)));
                                if (chunk.canRender(block)) {
                                    Face face = CubeMesh.getFrontFace(blockId);
                                    face.setColors(calculateColors(chunk.calculateLightLevel(x, y, z), currentBlock.getTint(BlockSide.FRONT)));
                                    meshBuilder.addFace(face, x, y, z);
                                }
                            }
                        }
                    }
                }
            }
            chunk.builder = meshBuilder;
            chunk.meshGenerated = true;
        }
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
}