package eu.zickzenni.opencubes.world.generation;

import eu.zickzenni.opencubes.world.Chunk;

public abstract class WorldGenerator {
    private int seed;

    public WorldGenerator(int seed) {
        this.seed = seed;
    }

    public abstract void generate(Chunk chunk);

    public int getSeed() {
        return seed;
    }
}
