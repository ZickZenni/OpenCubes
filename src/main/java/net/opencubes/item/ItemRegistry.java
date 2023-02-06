package net.opencubes.item;

import net.opencubes.block.BlockRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ItemRegistry {
    private static final Logger logger = LogManager.getLogger("Items");
    private static final Item[] items = new Item[256];

    public static final Item STONE = registerItem(new BlockItem(1, BlockRegistry.STONE));
    public static final Item GRASS_BLOCK = registerItem(new BlockItem(2, BlockRegistry.GRASS_BLOCK));
    public static final Item DIRT = registerItem(new BlockItem(3, BlockRegistry.DIRT));
    public static final Item SNOW_BLOCK = registerItem(new BlockItem(4, BlockRegistry.SNOW_BLOCK));
    public static final Item SAND = registerItem(new BlockItem(5, BlockRegistry.SAND));
    public static final Item COBBLESTONE = registerItem(new BlockItem(6, BlockRegistry.COBBLESTONE));
    public static final Item OAK_LOG = registerItem(new BlockItem(7, BlockRegistry.OAK_LOG));
    public static final Item BEDROCK = registerItem(new BlockItem(8, BlockRegistry.BEDROCK));

    public static void init() {
    }

    private static Item registerItem(Item item) {
        items[item.getItemId()] = item;
        logger.info("Item registered: " + item.getId() + " (" + item.getItemId() + ")");
        return item;
    }

    public static Item getItem(int id) {
        return items[id];
    }

    public static Item[] getItems() {
        return items;
    }
}
