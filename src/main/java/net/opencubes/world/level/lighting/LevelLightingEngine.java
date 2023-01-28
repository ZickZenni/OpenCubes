package net.opencubes.world.level.lighting;

import net.opencubes.world.level.Level;
import net.opencubes.world.level.chunk.LevelChunk;

public record LevelLightingEngine(Level level) {
    public static final float LIGHT_LEVEL = 1 / 16f;

    public void computeChunk(LevelChunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int lightLevel = level.getSkyLightLevel();
                for (int y = 255; y >= 0; y--) {
                    chunk.setLightLevel(x, y, z, 15);
                }
            }
        }
    }

    public static float computeLightLevel(int blockLightLevel) {
        return LIGHT_LEVEL * blockLightLevel + LIGHT_LEVEL;
    }
}