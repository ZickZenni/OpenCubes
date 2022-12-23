package eu.zickzenni.opencubes.world.generation;

import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.world.chunk.Chunk;

public class FlatworldGenerator extends WorldGenerator{
    public FlatworldGenerator(int seed) {
        super(seed);
    }

    @Override
    public void onGenerate(Chunk chunk) {
        for (int blockX = 0; blockX < 16; blockX++) {
            for (int blockZ = 0; blockZ < 16; blockZ++) {
                chunk.setBlock(blockX, 4, blockZ, Blocks.GRASS_BLOCK, true);
                chunk.setBlock(blockX, 3, blockZ, Blocks.DIRT, true);
                chunk.setBlock(blockX, 2, blockZ, Blocks.DIRT, true);
                chunk.setBlock(blockX, 1, blockZ, Blocks.DIRT, true);
                chunk.setBlock(blockX, 0, blockZ, Blocks.BEDROCK, true);
            }
        }
        chunk.generated = true;
    }
}
