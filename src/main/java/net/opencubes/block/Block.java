package net.opencubes.block;

import net.opencubes.world.level.Level;
import net.opencubes.world.level.chunk.ChunkBlock;
import net.opencubes.world.physics.Vec3;

import java.util.Objects;

public class Block {
    private final String name;
    private final BlockSound sound;

    public Block(String name, BlockSound sound) {
        this.name = name;
        this.sound = sound;
    }

    public void tick(ChunkBlock block) {
    }

    public boolean onBlockBreak(Level dimension, int x, int y, int z) {
        return true;
    }

    public boolean onBlockBreakByExplosion(Level dimension, int x, int y, int z) {
        return true;
    }

    public Vec3 getTint(BlockSide side) {
        return new Vec3(1, 1, 1);
    }

    public String getName() {
        return name;
    }

    public BlockSound getSound() {
        return sound;
    }

    public boolean isSolid() {
        return true;
    }

    public boolean isTransparent() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Objects.equals(name, block.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
