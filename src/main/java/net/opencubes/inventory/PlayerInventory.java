package net.opencubes.inventory;

import net.opencubes.item.ItemRegistry;
import org.joml.Math;

public class PlayerInventory extends Inventory {
    private int hotbarSlot = 0;

    public PlayerInventory() {
        super(36);
        setItemStack(27, new ItemStack(ItemRegistry.STONE));
        setItemStack(28, new ItemStack(ItemRegistry.GRASS_BLOCK));
        setItemStack(29, new ItemStack(ItemRegistry.DIRT));
        setItemStack(30, new ItemStack(ItemRegistry.SNOW_BLOCK));
        setItemStack(31, new ItemStack(ItemRegistry.SAND));
        setItemStack(32, new ItemStack(ItemRegistry.COBBLESTONE));
        setItemStack(33, new ItemStack(ItemRegistry.OAK_LOG));
        setItemStack(34, new ItemStack(ItemRegistry.BEDROCK));
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    public void setHotbarSlot(int slot) {
        this.hotbarSlot = Math.clamp(0, 8, slot);
    }
}