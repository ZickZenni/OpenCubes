package net.opencubes.item;

import net.opencubes.block.BlockSide;
import net.opencubes.client.vertex.Mesh;
import net.opencubes.world.level.Level;

public class Item {
    protected final int itemId;
    protected final String id;

    public Item(int itemId, String id) {
        this.itemId = itemId;
        this.id = id;
    }

    public boolean onBlockInteract(Level level, int x, int y, int z, BlockSide side) {
        return true;
    }

    public String getTranslationKey() {
        return "item." + id + ".name";
    }

    public int getMaxStack() {
        return 64;
    }

    public Mesh get3DModel(float size) {
        return null;
    }

    public final int getItemId() {
        return itemId;
    }

    public final String getId() {
        return id;
    }
}