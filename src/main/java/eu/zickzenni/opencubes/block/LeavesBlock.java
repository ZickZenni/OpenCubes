package eu.zickzenni.opencubes.block;

public class LeavesBlock extends Block {
    public LeavesBlock(int id, String name) {
        super(id, name, BlockSound.GRASS);
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
