package eu.zickzenni.opencubes.block;

import eu.zickzenni.opencubes.client.texture.AtlasManager;
import org.joml.Vector3f;

import java.util.Objects;

public class Block {
    private final byte id;
    private final String name;
    private final int fullTexture;
    private final boolean full;
    private final int sideTexture;
    private final int topTexture;
    private final int bottomTexture;

    private BlockSound sound;

    public Block(int id, String name, BlockSound sound) {
        this(id, name, sound, true);
    }

    public Block(int id, String name, BlockSound sound, boolean full) {
        this.id = (byte) id;
        this.name = name;
        this.sound = sound;
        this.full = full;
        this.fullTexture = AtlasManager.getTexture(name);
        this.sideTexture = AtlasManager.getTexture(name + "_side");
        this.topTexture = AtlasManager.getTexture(name + "_top");
        this.bottomTexture = AtlasManager.getTexture(name + "_bottom");
    }

    public Vector3f getTint(BlockSide side) {
        return new Vector3f(1,1,1);
    }

    public byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSolid() {
        return true;
    }

    public boolean isTransparent() {
        return false;
    }

    public boolean isUsingFullTexture() {
        return full;
    }

    public int getFullTexture() {
        return fullTexture;
    }

    public int getSideTexture() {
        return sideTexture;
    }

    public int getTopTexture() {
        return topTexture;
    }

    public int getBottomTexture() {
        return bottomTexture;
    }

    public BlockSound getSound() {
        return sound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return id == block.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
