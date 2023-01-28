package net.opencubes.client.block.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.opencubes.block.Block;
import net.opencubes.block.BlockRegistry;
import net.opencubes.util.ResourceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class BlockModelManager {
    private static final Logger LOGGER = LogManager.getLogger("BlockModelManager");

    private static HashMap<String, BlockModel> models = new HashMap<>();

    private BlockModelManager() {}

    public static void loadModels() {
        for (Block block : BlockRegistry.getBlocks()) {
            try {
                String data = ResourceUtil.loadResource("/assets/models/block/" + block.getName() + ".json");
                if (data == null) {
                    LOGGER.error("Block model for " + block.getName() + " does not exist!");
                    continue;
                }
                LOGGER.info("Loading block model for " + block.getName() + "...");

                HashMap<String, BlockModelTexture> textures = new HashMap<>();

                JsonObject object = JsonParser.parseString(data).getAsJsonObject();
                if (object.has("textures")) {
                    JsonObject texturesObject = object.get("textures").getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : texturesObject.entrySet()) {
                        textures.put(entry.getKey(), new BlockModelTexture(entry.getKey(), entry.getValue().getAsString()));
                    }
                }

                models.put(block.getName(), new BlockModel(textures));

                LOGGER.info("Loaded block model!");
            } catch (Exception e) {
                LOGGER.error("There was an error, while loading a block model: ", e);
            }
        }
    }

    public static BlockModel getModel(String name) {
        return models.getOrDefault(name, null);
    }

    public static Map<String, BlockModel> getModels() {
        return Collections.unmodifiableMap(models);
    }
}