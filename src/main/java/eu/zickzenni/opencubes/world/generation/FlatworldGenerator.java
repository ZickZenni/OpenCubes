package eu.zickzenni.opencubes.world.generation;

import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.world.Chunk;
import org.joml.Vector3i;

public class FlatworldGenerator extends WorldGenerator{
    public FlatworldGenerator(int seed) {
        super(seed);
    }

    @Override
    public void generate(Chunk chunk) {
        for (int blockX = 0; blockX < 16; blockX++) {
            for (int blockZ = 0; blockZ < 16; blockZ++) {
                chunk.setBlock(new Vector3i(blockX, 4, blockZ), Blocks.GRASS_BLOCK);
                chunk.setBlock(new Vector3i(blockX, 3, blockZ), Blocks.DIRT);
                chunk.setBlock(new Vector3i(blockX, 2, blockZ), Blocks.DIRT);
                chunk.setBlock(new Vector3i(blockX, 1, blockZ), Blocks.DIRT);
                chunk.setBlock(new Vector3i(blockX, 0, blockZ), Blocks.BEDROCK);
            }
        }
    }
}
