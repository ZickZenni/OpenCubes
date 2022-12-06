package eu.zickzenni.opencubes.block;

import java.util.HashMap;

public class Blocks {
    private static final HashMap<Byte, Block> blocks = new HashMap<>();

    public static final Block AIR = registerBlock(new Block(0, null));
    public static final Block STONE = registerBlock(new Block(1, new BlockTexture(2)));
    public static final Block GRASS_BLOCK = registerBlock(new GrassBlock(2, new BlockTexture(4, 1, 3)));
    public static final Block DIRT = registerBlock(new Block(3, new BlockTexture(3)));
    public static final Block SAND = registerBlock(new Block(12, new BlockTexture(19)));
    public static final Block OAK_LOG = registerBlock(new Block(17, new BlockTexture(21, 22, 22)));
    public static final Block OAK_LEAVES = registerBlock(new Block(18, new BlockTexture(53)));
    public static final Block BEDROCK = registerBlock(new Block(7, new BlockTexture(18)));

    private static Block registerBlock(Block block) {
        blocks.put(block.getId(), block);
        return block;
    }

    public static Block getBlock(int id) {
        return blocks.getOrDefault((byte)id, null);
    }
}