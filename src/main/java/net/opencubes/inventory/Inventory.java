package net.opencubes.inventory;

public abstract class Inventory {
    private ItemStack[] slots;

    public Inventory(int slots) {
        this.slots = new ItemStack[slots];
    }

    public ItemStack getItemStack(int index) {
        return slots[index];
    }

    public void setItemStack(int index, ItemStack itemStack) {
        slots[index] = itemStack;
    }

    public ItemStack[] getItemStacks() {
        return slots;
    }
}