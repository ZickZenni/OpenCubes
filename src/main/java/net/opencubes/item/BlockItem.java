package net.opencubes.item;

import net.opencubes.block.Block;
import net.opencubes.block.BlockSide;
import net.opencubes.client.vertex.Mesh;
import net.opencubes.client.vertex.util.CubeMesh;
import net.opencubes.world.level.Level;

public class BlockItem extends Item {
    private final Block block;

    public BlockItem(int itemId, Block block) {
        super(itemId, "block." + block.getName());
        this.block = block;
    }

    @Override
    public boolean onBlockInteract(Level level, int x, int y, int z, BlockSide side) {
        switch (side) {
            case FRONT -> level.placeBlock(x, y, z - 1, block);
            case BACK -> level.placeBlock(x, y, z + 1, block);
            case LEFT -> level.placeBlock(x - 1, y, z, block);
            case RIGHT -> level.placeBlock(x + 1, y, z, block);
            case TOP -> level.placeBlock(x, y + 1, z, block);
            case BOTTOM -> level.placeBlock(x, y - 1, z, block);
        }
        return false;
    }

    @Override
    public String getTranslationKey() {
        return id + ".name";
    }

    @Override
    public Mesh get3DModel(float size) {
        return CubeMesh.createCubeMesh(block.getName(), 1, 1, 1, 1, size, size, size,true);
    }
}