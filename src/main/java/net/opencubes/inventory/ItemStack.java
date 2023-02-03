package net.opencubes.inventory;

import net.opencubes.item.Item;
import org.joml.Math;

public class ItemStack {
    private final Item item;
    private int amount;

    public ItemStack(Item item) {
        this(item, 1);
    }

    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = Math.clamp(0, item.getMaxStack(), amount);
    }

    public Item getItem() {
        return item;
    }
}
