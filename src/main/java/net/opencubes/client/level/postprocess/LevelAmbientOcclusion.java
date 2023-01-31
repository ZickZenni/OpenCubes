package net.opencubes.client.level.postprocess;

import net.opencubes.block.BlockSide;
import net.opencubes.world.level.chunk.ChunkBlock;
import net.opencubes.world.level.chunk.LevelChunk;

public final class LevelAmbientOcclusion {
    private static float strength = 0.25f;
    private static boolean enabled = true;

    private LevelAmbientOcclusion() {
    }

    public static float[] compute(LevelChunk chunk, BlockSide side, int x, int y, int z) {
        if (!enabled) {
            return getDefaultAmbientOcclusion();
        }
        float[] ao = getDefaultAmbientOcclusion();
        switch (side) {
            case TOP -> {
                if (getBlockAt(chunk, x - 1, y + 1, z) != null || getBlockAt(chunk, x - 1, y + 1, z - 1) != null || getBlockAt(chunk, x, y + 1, z - 1) != null) {
                    ao[0] -= strength;
                }
                if (getBlockAt(chunk, x + 1, y + 1, z) != null || getBlockAt(chunk, x + 1, y + 1, z - 1) != null || getBlockAt(chunk, x, y + 1, z - 1) != null) {
                    ao[1] -= strength;
                }
                if (getBlockAt(chunk, x - 1, y + 1, z) != null || getBlockAt(chunk, x - 1, y + 1, z + 1) != null || getBlockAt(chunk, x, y + 1, z + 1) != null) {
                    ao[2] -= strength;
                }
                if (getBlockAt(chunk, x + 1, y + 1, z) != null || getBlockAt(chunk, x + 1, y + 1, z + 1) != null || getBlockAt(chunk, x, y + 1, z + 1) != null) {
                    ao[3] -= strength;
                }
            }
            case BOTTOM -> {
                if (getBlockAt(chunk, x - 1, y - 1, z) != null || getBlockAt(chunk, x - 1, y - 1, z - 1) != null || getBlockAt(chunk, x, y - 1, z - 1) != null) {
                    ao[0] -= strength;
                }
                if (getBlockAt(chunk, x + 1, y - 1, z) != null || getBlockAt(chunk, x + 1, y - 1, z - 1) != null || getBlockAt(chunk, x, y - 1, z - 1) != null) {
                    ao[1] -= strength;
                }
                if (getBlockAt(chunk, x - 1, y - 1, z) != null || getBlockAt(chunk, x - 1, y - 1, z + 1) != null || getBlockAt(chunk, x, y - 1, z + 1) != null) {
                    ao[2] -= strength;
                }
                if (getBlockAt(chunk, x + 1, y - 1, z) != null || getBlockAt(chunk, x + 1, y - 1, z + 1) != null || getBlockAt(chunk, x, y - 1, z + 1) != null) {
                    ao[3] -= strength;
                }
            }
            case FRONT -> {
                if (getBlockAt(chunk, x, y + 1, z + 1) != null) {
                    ao[0] -= strength;
                }
                if (getBlockAt(chunk, x, y - 1, z + 1) != null) {
                    ao[1] -= strength;
                }
                if (getBlockAt(chunk, x, y - 1, z + 1) != null) {
                    ao[2] -= strength;
                }
                if (getBlockAt(chunk, x, y + 1, z + 1) != null) {
                    ao[3] -= strength;
                }
            }
            case BACK -> {
                if (getBlockAt(chunk, x, y + 1, z - 1) != null) {
                    ao[0] -= strength;
                }
                if (getBlockAt(chunk, x, y - 1, z - 1) != null) {
                    ao[3] -= strength;
                }
                if (getBlockAt(chunk, x, y - 1, z - 1) != null) {
                    ao[2] -= strength;
                }
                if (getBlockAt(chunk, x, y + 1, z - 1) != null) {
                    ao[1] -= strength;
                }
            }
            case LEFT -> {
                if (getBlockAt(chunk, x - 1, y + 1, z) != null) {
                    ao[0] -= strength;
                }
                if (getBlockAt(chunk, x - 1, y - 1, z) != null) {
                    ao[1] -= strength;
                }
                if (getBlockAt(chunk, x - 1, y - 1, z) != null) {
                    ao[2] -= strength;
                }
                if (getBlockAt(chunk, x - 1, y + 1, z) != null) {
                    ao[3] -= strength;
                }
            }
            case RIGHT -> {
                if (getBlockAt(chunk, x + 1, y + 1, z) != null) {
                    ao[0] -= strength;
                }
                if (getBlockAt(chunk, x + 1, y - 1, z) != null) {
                    ao[1] -= strength;
                }
                if (getBlockAt(chunk, x + 1, y - 1, z) != null) {
                    ao[2] -= strength;
                }
                if (getBlockAt(chunk, x + 1, y + 1, z) != null) {
                    ao[3] -= strength;
                }
            }
        }
        return ao;
    }

    private static ChunkBlock getBlockAt(LevelChunk chunk, int x, int y, int z) {
        if (!chunk.inChunk(x,y,z)) {
            return chunk.getLevel().getBlock(chunk.getChunkPos().x() * 16 + x, y, chunk.getChunkPos().z() * 16 + z);
        }
        return chunk.getBlockAt(x,y,z);
    }

    public static float[] getDefaultAmbientOcclusion() {
        return new float[]{1, 1, 1, 1};
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static float getStrength() {
        return strength;
    }
}