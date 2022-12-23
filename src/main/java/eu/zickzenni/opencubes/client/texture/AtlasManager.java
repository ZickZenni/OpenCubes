package eu.zickzenni.opencubes.client.texture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AtlasManager {
    private static final Logger logger = LogManager.getLogger("AtlasManager");
    private static final HashMap<String, Integer> textures = new HashMap<>();

    public static void init() {
        logger.info("Registering atlas textures...");

        registerAtlasTexture("dirt", 3);
        registerAtlasTexture("grass_block_top", 1);
        registerAtlasTexture("grass_block_bottom", 3);
        registerAtlasTexture("grass_block_side", 4);
        registerAtlasTexture("stone", 2);
        registerAtlasTexture("bedrock", 18);
    }

    public static void registerAtlasTexture(String name, int i) {
        textures.put(name, i);
        logger.info("Atlas texture registered: " + name + " (" + i + ")");
    }

    public static int getTexture(String name) {
        return textures.getOrDefault(name, -1);
    }

    public static Map<String, Integer> getTextures() {
        return Collections.unmodifiableMap(textures);
    }
}