package eu.zickzenni.opencubes.world.generation;

import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.util.FastNoiseLite;
import eu.zickzenni.opencubes.world.Chunk;
import org.joml.Random;
import org.joml.Vector3i;

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
        caveNoise.SetFrequency(0.05f);

        random = new Random(seed);
    }

    @Override
    public void generate(Chunk chunk) {
        for (int blockX = 0; blockX < 16; blockX++) {
            for (int blockZ = 0; blockZ < 16; blockZ++) {
                int y = getHeight(blockX + (chunk.getX() * 16), blockZ + (chunk.getZ() * 16));
                chunk.setLightLevel(15, blockX, y,blockZ);

                if (!generateCave(blockX + (chunk.getX() * 16), y, blockZ + (chunk.getZ() * 16))) {
                    chunk.setBlock(new Vector3i(blockX, y, blockZ), Blocks.GRASS_BLOCK);
                }

                /*float treeChance = random.nextFloat();
                if (treeChance <= 0.006f) {
                    int treeSize = getRandomNumber(3, 6);
                    for (int i = 1; i < treeSize; i++) {
                        chunk.setBlock(new Vector3i(blockX, y + i, blockZ), Blocks.OAK_LOG);
                    }
                    chunk.setBlock(new Vector3i(blockX, y + treeSize, blockZ), Blocks.OAK_LEAVES);
                }*/

                for (int i = y - 1; i >= 1; i--) {
                    Vector3i position = new Vector3i(blockX, i, blockZ);
                    if (!generateCave(blockX + (chunk.getX() * 16), i, blockZ + (chunk.getZ() * 16))) {
                            chunk.setBlock(position, Blocks.STONE);
                        if (i >= y - 3) {
                            chunk.setBlock(position, Blocks.DIRT);
                        } else {
                            chunk.setBlock(position, Blocks.STONE);
                        }
                    }
                    chunk.setLightLevel(15, blockX, i,blockZ);
                    /*if (chunk.getBlockAt(position.x, position.y + 1, position.z) != Blocks.AIR.getId()) {
                        chunk.setLightLevel(0,position.x,  position.y, position.z);
                    } else  {
                        chunk.setLightLevel(15,position.x,  position.y, position.z);
                    }*/
                }
                chunk.setBlock(new Vector3i(blockX, 0, blockZ), Blocks.BEDROCK);
            }
        }
    }

    public int getHeight(int x, int z) {
        return (int) (((noise.GetNoise(x, z) * 1.5f) * (noise2.GetNoise(x, z)) * 6) * 30) + 80;
    }

    public boolean generateCave(int x, int y, int z) {
        float xy = caveNoise.GetNoise(x, y);
        float xz = caveNoise.GetNoise(x, z);
        float yz = caveNoise.GetNoise(y, z);
        float yx = caveNoise.GetNoise(y, x);
        float zx = caveNoise.GetNoise(z, x);
        float zy = caveNoise.GetNoise(z, y);

        float i = (xy + xz + yz + yx + zx + zy) / 6;
        return (i * 20) >= 4.5f;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((random.nextFloat() * (max - min)) + min);
    }
}
