package net.opencubes.world.level.biome;

import net.opencubes.block.BlockRegistry;
import net.opencubes.util.FastNoiseLite;
import net.opencubes.world.level.Level;

public class MountainsBiome extends Biome {
    private final FastNoiseLite noise;
    private final FastNoiseLite noiseHills;

    MountainsBiome(BiomeManager manager) {
        super(manager);
        this.noise = new FastNoiseLite(manager.getLevel().getSeed());
        this.noiseHills = new FastNoiseLite(manager.getLevel().getSeed());
        this.noiseHills.SetFrequency(0.0053f);
    }

    @Override
    public void generate(Level level, int x, int y, int z) {
        handleOcean(level, x, y, z);
        level.setBlock(x, y, z, BlockRegistry.GRASS_BLOCK);
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
        return (int) ((noise.GetNoise(x, z) * 3) * (noiseHills.GetNoise(x +  1511, z + 5151) * 8) + 97);
    }

    @Override
    public float getTemperatureThreshold() {
        return 1.229f;
    }

    @Override
    public float getHumidityThreshold() {
        return 0.74f;
    }
}