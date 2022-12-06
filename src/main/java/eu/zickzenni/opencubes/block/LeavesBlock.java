package eu.zickzenni.opencubes.block;

public class LeavesBlock extends Block {
    public LeavesBlock(int id, BlockTexture texture) {
        super(id, texture);
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}
