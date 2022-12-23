package eu.zickzenni.opencubes.client.world;

import eu.zickzenni.opencubes.util.SimpleProfiler;
import eu.zickzenni.opencubes.world.Dimension;
import eu.zickzenni.opencubes.world.chunk.Chunk;
import eu.zickzenni.opencubes.world.chunk.ChunkBlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class LightingEngine {
    private static final Logger logger = LogManager.getLogger("LightingEngine");

    /*public static boolean generateLightingDirect(Chunk chunk, int x, int y, int z, int lightLevel) {
        Dimension dimension = chunk.getDimension();
        if (y < 255) {
            if (y > 0) {
                chunk.setLightLevel(lightLevel, x, y, z);
                return chunk.doesBlockExist(x, y, z);
            } else {
                chunk.setLightLevel(lightLevel, x,y,z);
            }
        } else {
            chunk.setLightLevel(lightLevel, x,y,z);
        }
        return false;
    }*/

    private static long lastStatisticUpdate = -1;
    private static int chunkUpdates = 0;

    public static long timeTaken;

    /**
     * Updates the lighting of every block in the chunk in sync
     *
     * @param chunk
     */
    public static void updateChunkLighting(Chunk chunk) {
        if (lastStatisticUpdate == -1) {
            lastStatisticUpdate = System.currentTimeMillis();
        }
        chunkUpdates++;

        SimpleProfiler.start("LIGHTING_ENGINE");

        List<ChunkBlockPos> litBlocks = new ArrayList<>();

        Dimension dimension = chunk.getDimension();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        /*
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                byte lightLevel = chunk.getDimension().getSkyLightLevel();
                for (int y = 255; y >= 0; y--) {
                    if (y < 255) {
                        if (y > 0) {
                            if (chunk.doesBlockExist(x, y, z)) {
                                if (chunk.getLightLevel(x,y,z) == 0)
                                    lightLevel = 0;
                            } else {
                                if (chunk.getLightLevel(x,y,z) != 0) {
                                    lightLevel = chunk.getLightLevel(x,y,z);
                                }
                            }
                            chunk.setLightLevel(lightLevel, x, y, z);
                            if (lightLevel > 0) {
                                litBlocks.add(new ChunkBlockPos(chunk, x, y, z));
                            }
                            } else {
                                for (int p = 0; p <= 4; p++) {
                                    if (chunk.doesBlockExist(x, y, z)) {
                                        break;
                                    }
                                    int neighborX = x;
                                    int neighborY = y;
                                    int neighborZ = z;
                                    switch (p) {
                                        case 1 -> {
                                            neighborX = 15;
                                            if (!(x > 0)) {
                                                Chunk neighborChunk = dimension.getChunk(new ChunkPosition(chunkX - 1, chunkZ));
                                                if (neighborChunk != null && !neighborChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                                    byte neighbor = neighborChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                                    if (lightLevel < neighbor - 1) {
                                                        lightLevel = (byte) (neighbor - 1);
                                                        if (neighbor > 0) {
                                                            litBlocks.add(new ChunkBlockPos(chunk, x, y, z));
                                                        }
                                                    }

                                                    chunk.setLightLevel(lightLevel, x, y, z);
                                                }
                                            }
                                        }
                                        case 2 -> {
                                            neighborX = 0;
                                            if (!(x < 15)) {
                                                Chunk neighborChunk = dimension.getChunk(new ChunkPosition(chunkX + 1, chunkZ));
                                                if (neighborChunk != null && !neighborChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                                    byte neighbor = neighborChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                                    if (lightLevel < neighbor - 1) {
                                                        lightLevel = (byte) (neighbor - 1);
                                                        if (neighbor > 0) {
                                                            litBlocks.add(new ChunkBlockPos(chunk, x, y, z));
                                                        }
                                                    }

                                                    chunk.setLightLevel(lightLevel, x, y, z);
                                                }
                                            }
                                        }
                                        case 3 -> {
                                            neighborZ = 15;
                                            // Front
                                            if (!(z < 15)) {
                                                Chunk neighborChunk = dimension.getChunk(new ChunkPosition(chunkX, chunkZ - 1));
                                                if (neighborChunk != null && !neighborChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                                    byte neighbor = neighborChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                                    if (lightLevel < neighbor - 1) {
                                                        lightLevel = (byte) (neighbor - 1);
                                                        if (neighbor > 0) {
                                                            litBlocks.add(new ChunkBlockPos(chunk, x, y, z));
                                                        }
                                                    }

                                                    chunk.setLightLevel(lightLevel, x, y, z);
                                                }
                                            }
                                        }
                                        case 4 -> {
                                            neighborZ = 0;
                                            // Back
                                            if (!(z > 0)) {
                                                Chunk neighborChunk = dimension.getChunk(new ChunkPosition(chunkX, chunkZ + 1));
                                                if (neighborChunk != null && !neighborChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                                    byte neighbor = neighborChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                                    if (lightLevel < neighbor - 1) {
                                                        lightLevel = (byte) (neighbor - 1);
                                                        if (neighbor > 0) {
                                                            litBlocks.add(new ChunkBlockPos(chunk, x, y, z));
                                                        }
                                                    }

                                                    chunk.setLightLevel(lightLevel, x, y, z);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            chunk.setLightLevel(lightLevel, x, y, z);
                        }
                    }
                }
            }
        }
        while (litBlocks.size() > 0) {
            ChunkBlockPos pos = litBlocks.get(litBlocks.size() - 1);
            litBlocks.remove(litBlocks.size() - 1);
            Chunk posChunk = pos.getChunk();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            byte lightLevel = chunk.getLightLevel(x, y, z);

            for (int p = 0; p < 6; p++) {
                int neighborX = x;
                int neighborY = y;
                int neighborZ = z;
                switch (p) {
                    case 1:
                        neighborX = x - 1;
                        if (x > 0) {
                            if (Chunk.inChunk(neighborX, neighborY, neighborZ) && !posChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = posChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    if (neighbor > 0) {
                                        litBlocks.add(new ChunkBlockPos(posChunk, neighborX, neighborY, neighborZ));
                                    }
                                }

                                posChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        } else {
                            neighborX = 15;
                            Chunk neighborChunk = dimension.getChunk(new ChunkPosition(chunkX - 1, chunkZ));
                            if (neighborChunk != null && !neighborChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = neighborChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    //if (neighbor > 0) {
                                    //    litBlocks.add(new ChunkBlockPos(neighborChunk, neighborX, neighborY, neighborZ));
                                    //}
                                }

                                posChunk.setLightLevel(lightLevel, x, y, z);
                                neighborChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        }
                        break;
                    case 2:
                        neighborX = x + 1;
                        if (x < 15) {
                            if (Chunk.inChunk(neighborX, neighborY, neighborZ) && !posChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = posChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    if (neighbor > 0) {
                                        litBlocks.add(new ChunkBlockPos(posChunk, neighborX, neighborY, neighborZ));
                                    }
                                }

                                posChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        } else {
                            neighborX = 0;
                            Chunk neighborChunk = dimension.getChunk(new ChunkPosition(chunkX + 1, chunkZ));
                            if (neighborChunk != null && !neighborChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = neighborChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    //if (neighbor > 0) {
                                    //    litBlocks.add(new ChunkBlockPos(neighborChunk, neighborX, neighborY, neighborZ));
                                    //}
                                }

                                posChunk.setLightLevel(lightLevel, x, y, z);
                                neighborChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        }
                        break;
                    case 3:
                        neighborY = y - 1;
                        if (y > 0) {
                            if (Chunk.inChunk(neighborX, neighborY, neighborZ) && !posChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = posChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    if (neighbor > 0) {
                                        litBlocks.add(new ChunkBlockPos(posChunk, neighborX, neighborY, neighborZ));
                                    }
                                }

                                posChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        }
                        break;
                    case 4:
                        // Top
                        if (y < 254) {
                            neighborY = y + 1;
                            if (Chunk.inChunk(neighborX, neighborY, neighborZ) && !posChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = posChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    if (neighbor > 0) {
                                        litBlocks.add(new ChunkBlockPos(posChunk, neighborX, neighborY, neighborZ));
                                    }
                                }

                                posChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        }
                        break;
                    case 5:
                        neighborZ = z - 1;

                        // Front
                        if (z < 15) {
                            if (Chunk.inChunk(neighborX, neighborY, neighborZ) && !posChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = posChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    if (neighbor > 0) {
                                        litBlocks.add(new ChunkBlockPos(posChunk, neighborX, neighborY, neighborZ));
                                    }
                                }

                                posChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        } else {
                            neighborZ = 15;
                            Chunk neighborChunk = dimension.getChunk(new ChunkPosition(chunkX, chunkZ - 1));
                            if (neighborChunk != null && !neighborChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = neighborChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    //if (neighbor > 0) {
                                    //    litBlocks.add(new ChunkBlockPos(neighborChunk, neighborX, neighborY, neighborZ));
                                    //}
                                }

                                posChunk.setLightLevel(lightLevel, x, y, z);
                                neighborChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        }
                        break;
                    case 6:
                        neighborZ = z + 1;
                        // Back
                        if (z > 0) {
                            if (Chunk.inChunk(neighborX, neighborY, neighborZ) && !posChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = posChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    if (neighbor > 0) {
                                        litBlocks.add(new ChunkBlockPos(posChunk, neighborX, neighborY, neighborZ));
                                    }
                                }

                                posChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        } else {
                            neighborZ = 0;
                            Chunk neighborChunk = dimension.getChunk(new ChunkPosition(chunkX, chunkZ + 1));
                            if (neighborChunk != null && !neighborChunk.doesBlockExist(neighborX, neighborY, neighborZ)) {
                                byte neighbor = neighborChunk.getLightLevel(neighborX, neighborY, neighborZ);
                                if (neighbor < lightLevel - 1) {
                                    neighbor = (byte) (lightLevel - 1);
                                    //if (neighbor > 0) {
                                    //    litBlocks.add(new ChunkBlockPos(neighborChunk, neighborX, neighborY, neighborZ));
                                    //}
                                }
                                posChunk.setLightLevel(lightLevel, x, y, z);
                                neighborChunk.setLightLevel(neighbor, neighborX, neighborY, neighborZ);
                            }
                        }
                        break;
                }
            }
        }
        */
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                byte lightLevel = chunk.getDimension().getSkyLightLevel();
                for (int y = 255; y >= 0; y--) {
                    chunk.setLightLevel(lightLevel, x, y, z);
                }
            }
        }

        timeTaken = SimpleProfiler.stop("LIGHTING_ENGINE");
    }


    public static void update() {
        if (System.currentTimeMillis() - lastStatisticUpdate >= 10000) {
            if (chunkUpdates != 0) {
                logger.info("Performed " + chunkUpdates + (chunkUpdates == 1 ? " update " : " updates ") + "in the last 10 seconds");
            }
            lastStatisticUpdate = System.currentTimeMillis();
            chunkUpdates = 0;
        }
    }
}
