package net.opencubes.block;

import net.opencubes.world.physics.Vec3;

public class GrassBlock extends Block {
    public GrassBlock(String name, BlockSound sound) {
        super(name, sound);
    }

    @Override
    public Vec3 getTint(BlockSide side) {
        if (side == BlockSide.TOP) {
            float r = 141 / 255f;
            float g = 201 / 255f;
            float b = 88 / 255f;
            return new Vec3(r * 0.85f,g * 0.85f,b * 0.85f);
        }
        return super.getTint(side);
    }
}