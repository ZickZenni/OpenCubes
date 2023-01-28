package net.opencubes.client.block.model;

import java.util.HashMap;

public class BlockModel {
    private final HashMap<String, BlockModelTexture> textures;

    public BlockModel(HashMap<String, BlockModelTexture> textures) {
        this.textures = textures;
    }

    public HashMap<String, BlockModelTexture> getTextures() {
        return textures;
    }
}
