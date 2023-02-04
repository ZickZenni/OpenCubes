package net.opencubes.world.level.biome;

import net.opencubes.block.BlockRegistry;
import net.opencubes.util.FastNoiseLite;
import net.opencubes.world.level.Level;

public class SnowyPlainsBiome extends Biome {
    private final FastNoiseLite noise;

    SnowyPlainsBiome(BiomeManager manager) {
        super(manager);
        this.noise = new FastNoiseLite(manager.getLevel().getSeed());
        this.noise.SetFrequency(0.006f);
    }

    @Override
    public void generate(Level level, int x, int y, int z) {
        level.setBlock(x, y, z, BlockRegistry.SNOW_BLOCK);
        for (int yD = y - 1; yD > 0; yD--) {
            if (yD >= y - 3) {
                level.setBlock(x, yD, z, BlockRegistry.DIRT);
            } else {
                level.setBlock(x, yD, z, BlockRegistry.STONE);
            }
        }
        level.setBlock(x, 0, z, BlockRegistry.BEDROCK);
    }

    @Override
    public int getHeight(Level level, int x, int z) {
        return (int) (noise.GetNoise(x, z) * 23) + 96;
    }

    @Override
    public float getTemperatureThreshold() {
        return 0.25f;
    }

    @Override
    public float getHumidityThreshold() {
        return 0.32f;
    }
}