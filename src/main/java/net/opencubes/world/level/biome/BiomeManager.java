package net.opencubes.world.level.biome;

import net.opencubes.util.FastNoiseLite;
import net.opencubes.world.level.Level;
import net.opencubes.world.level.chunk.LevelChunk;

import java.util.ArrayList;

public class BiomeManager {
    private final Level level;
    private ArrayList<Biome> biomes = new ArrayList<Biome>();

    private final OceanBiome oceanBiome;

    private final FastNoiseLite temperatureNoise;
    private final FastNoiseLite humidityNoise;

    public BiomeManager(Level level) {
        this.level = level;
        this.temperatureNoise = new FastNoiseLite(level.getSeed());
        this.temperatureNoise.SetFractalOctaves(3);
        this.temperatureNoise.SetFrequency(0.0012f);
        this.humidityNoise = new FastNoiseLite(level.getSeed() + 5195);
        this.humidityNoise.SetFractalOctaves(2);
        this.humidityNoise.SetFrequency(0.006f);

        this.oceanBiome = new OceanBiome(this);
        this.biomes.add(new PlainsBiome(this));
        this.biomes.add(new FlatPlainsBiome(this));
        this.biomes.add(new MountainsBiome(this));
        this.biomes.add(new SnowyPlainsBiome(this));
    }

    public void generate(LevelChunk chunk) {
        int[][] heightmap = getHeightMap(chunk);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                Biome biome = getBiome(x + chunk.getChunkPos().x() * 16, z + chunk.getChunkPos().z() * 16);
                if (!sameBiomes(chunk)) {
                    biome.generate(level, x + chunk.getChunkPos().x() * 16, heightmap[x][z], z + chunk.getChunkPos().z() * 16);
                } else {
                    biome.generate(level, x + chunk.getChunkPos().x() * 16, getHeight(x + chunk.getChunkPos().x() * 16, z + chunk.getChunkPos().z() * 16), z + chunk.getChunkPos().z() * 16);
                }
            }
        }
    }

    private int[][] getHeightMap(LevelChunk chunk) {
        int[][] heightmap = new int[16][16];
        getHeightIn(0, 0, 8, 8, chunk, heightmap);
        getHeightIn(8, 0, 16, 8, chunk, heightmap);
        getHeightIn(0, 8, 8, 16, chunk, heightmap);
        getHeightIn(8, 8, 16, 16, chunk, heightmap);
        return heightmap;
    }

    private void getHeightIn(int xMin, int zMin, int xMax, int zMax, LevelChunk chunk, int[][] heightmap) {
        int bottomLeft = getHeight(xMin + chunk.getChunkPos().x() * 16, zMin + chunk.getChunkPos().z() * 16);
        int bottomRight = getHeight(xMax + chunk.getChunkPos().x() * 16, zMin + chunk.getChunkPos().z() * 16);
        int topLeft = getHeight(xMin + chunk.getChunkPos().x() * 16, zMax + chunk.getChunkPos().z() * 16);
        int topRight = getHeight(xMax + chunk.getChunkPos().x() * 16, zMax + chunk.getChunkPos().z() * 16);

        for (int x = xMin; x < xMax; ++x) {
            for (int z = zMin; z < zMax; ++z) {
                if (x == 16) continue;
                if (z == 16) continue;

                float h = smoothInterpolation(bottomLeft, topLeft, bottomRight, topRight, xMin, xMax, zMin, zMax, x, z);

                heightmap[x][z] = (int) h;
            }
        }
    }

    private boolean sameBiomes(LevelChunk chunk) {
        ArrayList<Biome> biomes = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                Biome biome = getBiome((chunk.getChunkPos().x() + x) * 16, (chunk.getChunkPos().z() + z) * 16);
                if (biomes.size() == 0) {
                    biomes.add(biome);
                } else {
                    if (!biomes.contains(biome)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*private boolean sameBiomes(LevelChunk chunk) {
        Biome topLeft = getBiome(chunk.getChunkPos().x() * 16, 15 + chunk.getChunkPos().z() * 16);
        Biome topRight = getBiome(15 + chunk.getChunkPos().x() * 16, 15 + chunk.getChunkPos().z() * 16);
        Biome bottomLeft = getBiome(chunk.getChunkPos().x() * 16, chunk.getChunkPos().z() * 16);
        Biome bottomRight = getBiome(15 + chunk.getChunkPos().x() * 16, chunk.getChunkPos().z() * 16);
        return topLeft == topRight && topRight == bottomLeft && bottomLeft == bottomRight;
    }*/

    private float smoothstep(float edge0, float edge1, float x) {
        // Scale, bias and saturate x to 0..1 range
        x = x * x * (3 - 2 * x);
        // Evaluate polynomial
        return (edge0 * x) + (edge1 * (1 - x));
    }

    private float smoothInterpolation(float bottomLeft, float topLeft, float bottomRight, float topRight, float xMin, float xMax, float zMin, float zMax, float x, float z) {
        float width = xMax - xMin, height = zMax - zMin;
        float xValue = 1 - (x - xMin) / width;
        float zValue = 1 - (z - zMin) / height;

        float a = smoothstep(bottomLeft, bottomRight, xValue);
        float b = smoothstep(topLeft, topRight, xValue);
        return smoothstep(a, b, zValue);
    }

    private float bilinearInterpolation(float bottomLeft, float topLeft, float bottomRight, float topRight, float xMin, float xMax, float zMin, float zMax, float x, float z) {
        float width = xMax - xMin, height = zMax - zMin, xDistanceToMaxValue = xMax - x, zDistanceToMaxValue = zMax - z, xDistanceToMinValue = x - xMin, zDistanceToMinValue = z - zMin;

        return 1.0f / (width * height) * (bottomLeft * xDistanceToMaxValue * zDistanceToMaxValue + bottomRight * xDistanceToMinValue * zDistanceToMaxValue + topLeft * xDistanceToMaxValue * zDistanceToMinValue + topRight * xDistanceToMinValue * zDistanceToMinValue);
    }

    private int getHeight(int x, int z) {
        Biome biome = getBiome(x, z);
        if (biome != null) {
            return biome.getHeight(level, x, z);
        } else {
            return this.biomes.get(0).getHeight(level, x, z);
        }
    }

    private Biome getBiome(int x, int z) {
        float temperature = temperatureNoise.GetNoise(x, z) + 1;
        float humidity = humidityNoise.GetNoise(x, z) + 1;

        ArrayList<Biome> biomes = getBiomesBetweenThreshold(temperature, humidity);
        Biome nearest = getNearestBiome(biomes, temperature, humidity);
        if (nearest == null) {
            nearest = oceanBiome;
        }
        return nearest;
    }

    private ArrayList<Biome> getBiomesBetweenThreshold(float temperature, float humidity) {
        ArrayList<Biome> biomes = new ArrayList<>();
        for (Biome biome : this.biomes) {
            if (temperature <= biome.getTemperatureThreshold() && humidity <= biome.getHumidityThreshold()) {
                biomes.add(biome);
            }
        }
        return biomes;
    }

    private Biome getNearestBiome(ArrayList<Biome> biomes, float temperature, float humidity) {
        Biome current = null;
        for (Biome biome : biomes) {
            if (current == null) {
                current = biome;
            } else {
                float temperatureDistance = biome.getTemperatureThreshold() - temperature;
                float humidityDistance = biome.getHumidityThreshold() - humidity;

                float currentTemperatureDistance = current.getTemperatureThreshold() - temperature;
                float currentHumidityDistance = current.getHumidityThreshold() - humidity;

                if (temperatureDistance < currentTemperatureDistance && humidityDistance < currentHumidityDistance) {
                    current = biome;
                }
            }
        }
        return current;
    }

    public Level getLevel() {
        return level;
    }
}
