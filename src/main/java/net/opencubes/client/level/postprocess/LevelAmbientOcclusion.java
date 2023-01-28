package net.opencubes.client.level.postprocess;

import net.opencubes.block.BlockSide;
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
                if (chunk.getBlockAt(x - 1, y + 1, z) != null || chunk.getBlockAt(x - 1, y + 1, z - 1) != null || chunk.getBlockAt(x, y + 1, z - 1) != null) {
                    ao[0] -= strength;
                }
                if (chunk.getBlockAt(x + 1, y + 1, z) != null || chunk.getBlockAt(x + 1, y + 1, z - 1) != null || chunk.getBlockAt(x, y + 1, z - 1) != null) {
                    ao[1] -= strength;
                }
                if (chunk.getBlockAt(x - 1, y + 1, z) != null || chunk.getBlockAt(x - 1, y + 1, z + 1) != null || chunk.getBlockAt(x, y + 1, z + 1) != null) {
                    ao[2] -= strength;
                }
                if (chunk.getBlockAt(x + 1, y + 1, z) != null || chunk.getBlockAt(x + 1, y + 1, z + 1) != null || chunk.getBlockAt(x, y + 1, z + 1) != null) {
                    ao[3] -= strength;
                }
            }
            case BOTTOM -> {
                if (chunk.getBlockAt(x - 1, y - 1, z) != null || chunk.getBlockAt(x - 1, y - 1, z - 1) != null || chunk.getBlockAt(x, y - 1, z - 1) != null) {
                    ao[0] -= strength;
                }
                if (chunk.getBlockAt(x + 1, y - 1, z) != null || chunk.getBlockAt(x + 1, y - 1, z - 1) != null || chunk.getBlockAt(x, y - 1, z - 1) != null) {
                    ao[1] -= strength;
                }
                if (chunk.getBlockAt(x - 1, y - 1, z) != null || chunk.getBlockAt(x - 1, y - 1, z + 1) != null || chunk.getBlockAt(x, y - 1, z + 1) != null) {
                    ao[2] -= strength;
                }
                if (chunk.getBlockAt(x + 1, y - 1, z) != null || chunk.getBlockAt(x + 1, y - 1, z + 1) != null || chunk.getBlockAt(x, y - 1, z + 1) != null) {
                    ao[3] -= strength;
                }
            }
            case FRONT -> {
                if (chunk.getBlockAt(x, y + 1, z + 1) != null) {
                    ao[0] -= strength;
                }
                if (chunk.getBlockAt(x, y - 1, z + 1) != null) {
                    ao[1] -= strength;
                }
                if (chunk.getBlockAt(x, y - 1, z + 1) != null) {
                    ao[2] -= strength;
                }
                if (chunk.getBlockAt(x, y + 1, z + 1) != null) {
                    ao[3] -= strength;
                }
            }
            case BACK -> {
                if (chunk.getBlockAt(x, y + 1, z - 1) != null) {
                    ao[0] -= strength;
                }
                if (chunk.getBlockAt(x, y - 1, z - 1) != null) {
                    ao[3] -= strength;
                }
                if (chunk.getBlockAt(x, y - 1, z - 1) != null) {
                    ao[2] -= strength;
                }
                if (chunk.getBlockAt(x, y + 1, z - 1) != null) {
                    ao[1] -= strength;
                }
            }
            case LEFT -> {
                if (chunk.getBlockAt(x - 1, y + 1, z) != null) {
                    ao[0] -= strength;
                }
                if (chunk.getBlockAt(x - 1, y - 1, z) != null) {
                    ao[1] -= strength;
                }
                if (chunk.getBlockAt(x - 1, y - 1, z) != null) {
                    ao[2] -= strength;
                }
                if (chunk.getBlockAt(x - 1, y + 1, z) != null) {
                    ao[3] -= strength;
                }
            }
            case RIGHT -> {
                if (chunk.getBlockAt(x + 1, y + 1, z) != null) {
                    ao[0] -= strength;
                }
                if (chunk.getBlockAt(x + 1, y - 1, z) != null) {
                    ao[1] -= strength;
                }
                if (chunk.getBlockAt(x + 1, y - 1, z) != null) {
                    ao[2] -= strength;
                }
                if (chunk.getBlockAt(x + 1, y + 1, z) != null) {
                    ao[3] -= strength;
                }
            }
        }
        return ao;
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