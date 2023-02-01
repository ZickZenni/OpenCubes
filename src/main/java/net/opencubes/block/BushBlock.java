package net.opencubes.block;

public class BushBlock extends Block {
    public BushBlock(String name) {
        super(name, BlockSound.GRASS);
    }

    @Override
    public String shader() {
        return "foliage";
    }
}
