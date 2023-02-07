package net.opencubes.world.level.biome;

import net.opencubes.block.BlockRegistry;
import net.opencubes.world.level.Level;

public abstract class Biome {
    protected final BiomeManager manager;

    Biome(BiomeManager manager) {
        this.manager = manager;
    }

    public abstract void generate(Level level, int x, int y, int z);
    public abstract int getHeight(Level level, int x, int z);

    public abstract float getTemperatureThreshold();
    public abstract float getHumidityThreshold();

    protected void handleOcean(Level level, int x, int y, int z) {
        int waterLevel = OceanBiome.WATER_LEVEL;
        if (y >= waterLevel) {
            return;
        }
        for (int yW = waterLevel; yW > y; yW--) {
            if (level.getBlock(x, yW, z) == null) {
                level.setBlock(x, yW, z, BlockRegistry.WATER);
            }
        }
    }
}