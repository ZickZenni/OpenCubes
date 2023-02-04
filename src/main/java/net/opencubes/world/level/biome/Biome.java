package net.opencubes.world.level.biome;

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
}