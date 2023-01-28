package net.opencubes.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockRegistry {
    private static final Logger logger = LogManager.getLogger("Blocks");
    private static final ArrayList<Block> blocks = new ArrayList<>();

    public static final Block STONE = registerBlock(new Block("stone", BlockSound.STONE));
    public static final Block DIRT = registerBlock(new Block("dirt", BlockSound.GRAVEL));
    public static final Block GRASS_BLOCK = registerBlock(new GrassBlock("grass_block", BlockSound.GRASS));
    public static final Block COBBLESTONE = registerBlock(new Block("cobblestone", BlockSound.STONE));
    public static final Block OAK_LOG = registerBlock(new Block("oak_log", BlockSound.WOOD));
    public static final Block OAK_WOOD = registerBlock(new Block("oak_wood", BlockSound.WOOD));
    public static final Block BEDROCK = registerBlock(new Block("bedrock", BlockSound.STONE));

    public static void init() {
    }

    private static Block registerBlock(Block block) {
        blocks.add(block);
        logger.info("Block registered: " + block.getName());
        return block;
    }

    public static Block getBlock(String name) {
        for (Block block : blocks) {
            if (block.getName().equals(name)) {
                return block;
            }
        }
        return null;
    }

    public static List<Block> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }
}