package net.opencubes.block;

public class FluidBlock extends Block {
    public FluidBlock(String name, BlockSound sound) {
        super(name, sound);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean isFluid() {
        return true;
    }

    @Override
    public String shader() {
        return "fluid";
    }
}
