package eu.zickzenni.opencubes.block;

import org.joml.Vector3f;

public class GrassBlock extends Block {
    public GrassBlock(int id, String name) {
        super(id, name, BlockSound.GRASS, false);
    }

    @Override
    public Vector3f getTint(BlockSide side) {
        if (side == BlockSide.TOP) {
            float r = 178 / 255f;
            float g = 255 / 255f;
            float b = 112 / 255f;
            return new Vector3f(r,g,b);
        }
        return super.getTint(side);
    }
}