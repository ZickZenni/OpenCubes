package eu.zickzenni.opencubes.world.generation;

import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.util.FastNoiseLite;
import eu.zickzenni.opencubes.world.chunk.Chunk;
import org.joml.Random;

public class OverworldGenerator extends WorldGenerator{
    private FastNoiseLite noise;
    private FastNoiseLite noise2;

    private FastNoiseLite biomeNoise;
    private FastNoiseLite caveNoise;

    private Random random;

    public OverworldGenerator(int seed) {
        super(seed);
        noise = new FastNoiseLite();
        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        noise.SetFrequency(0.012f);
        noise.SetFractalOctaves(5);
        noise.SetFractalType(FastNoiseLite.FractalType.FBm);
        noise.SetSeed(seed);

        noise2 = new FastNoiseLite();
        noise2.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        noise2.SetFrequency(0.0086f);
        noise2.SetFractalOctaves(3);
        noise2.SetFractalType(FastNoiseLite.FractalType.FBm);
        noise2.SetSeed(seed);

        biomeNoise = new FastNoiseLite();
        biomeNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        biomeNoise.SetSeed(seed + 15018);

        caveNoise = new FastNoiseLite();
        caveNoise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        caveNoise.SetSeed(seed + 512);
        caveNoise.SetFrequency(0.01f);

        random = new Random(seed);
    }

    @Override
    public void onGenerate(Chunk chunk) {
        for (int blockX = 0; blockX < 16; blockX++) {
            for (int blockZ = 0; blockZ < 16; blockZ++) {
                int y = getHeight(blockX + (chunk.getX() * 16), blockZ + (chunk.getZ() * 16));
                //chunk.setLightLevel(15, blockX, y,blockZ);

                if (!generateCave(blockX + (chunk.getX() * 16), y, blockZ + (chunk.getZ() * 16))) {
                    chunk.setBlock(blockX, y, blockZ, Blocks.GRASS_BLOCK, true);
                }

                /*float treeChance = random.nextFloat();
                if (treeChance <= 0.006f) {
                    int treeSize = getRandomNumber(3, 6);
                    for (int i = 1; i < treeSize; i++) {
                        chunk.setBlock(blockX, y + i, blockZ), Blocks.OAK_LOG);
                    }
                    chunk.setBlock(blockX, y + treeSize, blockZ), Blocks.OAK_LEAVES);
                }*/

                for (int i = y - 1; i >= 1; i--) {
                    if (!generateCave(blockX + (chunk.getX() * 16), i, blockZ + (chunk.getZ() * 16))) {
                            chunk.setBlock(blockX, i, blockZ, Blocks.STONE, true);
                        if (i >= y - 3) {
                            chunk.setBlock(blockX, i, blockZ, Blocks.DIRT, true);
                        } else {
                            chunk.setBlock(blockX, i, blockZ, Blocks.STONE, true);
                        }
                    }
                }
                chunk.setBlock(blockX, 0, blockZ, Blocks.BEDROCK, true);
            }
        }
        chunk.generated = true;
    }

    public int getHeight(int x, int z) {
        return (int) (((noise.GetNoise(x, z) * 1.5f) * (noise2.GetNoise(x, z)) * 6) * 30) + 80;
    }

    public float perlin3d(int x, int y, int z, int offset) {
        float xy = caveNoise.GetNoise(x + offset, y + offset);
        float xz = caveNoise.GetNoise(x + offset, z + offset);
        float yz = caveNoise.GetNoise(y + offset, z + offset);
        float yx = caveNoise.GetNoise(y + offset, x + offset);
        float zx = caveNoise.GetNoise(z + offset, x + offset);
        float zy = caveNoise.GetNoise(z + offset, y + offset);

        return (xy + xz + yz + yx + zx + zy) / 6;
    }

    private boolean generateCave(int x, int y, int z) {
        double a = 0.0094;
        if (y <= 40) {
            a += (0.015 / 32) * y;
        }
        if (y <= 8) {
            a -= (0.2 / 8) * y;
        }

        return Math.abs(perlin3d(x, y, z, 0)) < a && Math.abs(perlin3d(x,y,z, 51897)) < a;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((random.nextFloat() * (max - min)) + min);
    }
}
