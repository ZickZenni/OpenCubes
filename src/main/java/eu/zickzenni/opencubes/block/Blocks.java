package eu.zickzenni.opencubes.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Blocks {
    private static final Logger logger = LogManager.getLogger("Blocks");
    private static final HashMap<Byte, Block> blocks = new HashMap<>();

    public static final Block AIR = registerBlock(new Block(0, "air", BlockSound.NONE));
    public static final Block STONE = registerBlock(new Block(1, "stone", BlockSound.STONE));
    public static final Block GRASS_BLOCK = registerBlock(new GrassBlock(2, "grass_block"));
    public static final Block DIRT = registerBlock(new Block(3, "dirt", BlockSound.GRAVEL));
    public static final Block SAND = registerBlock(new Block(12, "sand", BlockSound.NONE));
    public static final Block OAK_LOG = registerBlock(new Block(17, "oak_log", BlockSound.NONE));
    public static final Block OAK_LEAVES = registerBlock(new Block(18, "oak_leaves", BlockSound.NONE));
    public static final Block BEDROCK = registerBlock(new Block(7, "bedrock", BlockSound.NONE));

    public static void init() {
    }

    private static Block registerBlock(Block block) {
        blocks.put(block.getId(), block);
        logger.info("Block registered: " + block.getName() + " (" + block.getId() + ")");
        return block;
    }

    public static Block getBlock(int id) {
        return blocks.getOrDefault((byte)id, null);
    }
}