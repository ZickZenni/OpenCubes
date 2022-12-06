package eu.zickzenni.opencubes.block;

public enum BlockSide {
    FRONT,
    BACK,
    LEFT,
    RIGHT,
    TOP,
    BOTTOM;

    public static boolean isSide(BlockSide side) {
        return side == LEFT || side == RIGHT || side == FRONT || side == BACK;
    }
}
