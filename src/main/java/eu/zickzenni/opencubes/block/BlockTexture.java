package eu.zickzenni.opencubes.block;

import java.util.HashMap;

public class BlockTexture {
    private boolean singleTexture;
    private int singleTextureAtlasPosition;

    private HashMap<BlockSide, Integer> atlasPositions = new HashMap<>();

    public BlockTexture(int atlasPosition) {
        this.singleTexture = true;
        this.singleTextureAtlasPosition = atlasPosition;
    }

    public BlockTexture(int sideAtlasPosition, int topAtlasPosition, int bottomAtlasPosition) {
        atlasPositions.put(BlockSide.LEFT, sideAtlasPosition);
        atlasPositions.put(BlockSide.RIGHT, sideAtlasPosition);
        atlasPositions.put(BlockSide.FRONT, sideAtlasPosition);
        atlasPositions.put(BlockSide.BACK, sideAtlasPosition);

        atlasPositions.put(BlockSide.TOP, topAtlasPosition);
        atlasPositions.put(BlockSide.BOTTOM, bottomAtlasPosition);
    }

    public boolean isSingleTexture() {
        return singleTexture;
    }

    public int getSingleAtlasPosition() {
        return singleTextureAtlasPosition;
    }

    public HashMap<BlockSide, Integer> getAtlasPositions() {
        return atlasPositions;
    }
}
