package eu.zickzenni.opencubes.block;

import org.joml.Vector3f;

public class Block {
    private final byte id;
    private final BlockTexture texture;

    public Block(int id, BlockTexture texture) {
        this.id = (byte) id;
        this.texture = texture;
    }

    public Vector3f getTint(BlockSide side) {
        return new Vector3f(1,1,1);
    }

    public byte getId() {
        return id;
    }

    public boolean isAir() {
        return id == 0;
    }

    public boolean isTransparent() {
        return false;
    }

    public BlockTexture getTexture() {
        return texture;
    }
}
