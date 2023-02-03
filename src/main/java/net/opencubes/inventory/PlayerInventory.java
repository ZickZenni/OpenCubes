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
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    public void setHotbarSlot(int slot) {
        this.hotbarSlot = Math.clamp(0, 8, slot);
    }
}