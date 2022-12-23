package eu.zickzenni.opencubes.world.generation;

import eu.zickzenni.opencubes.world.chunk.Chunk;

public abstract class WorldGenerator {
    private int seed;

    public WorldGenerator(int seed) {
        this.seed = seed;
    }

    public void generate(Chunk chunk) {
        onGenerate(chunk);
    }

    public abstract void onGenerate(Chunk chunk);

    public int getSeed() {
        return seed;
    }
}
