package eu.zickzenni.opencubes.client.block;

import eu.zickzenni.opencubes.util.ResourceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class BlockModelManager {
    private static final Logger logger = LogManager.getLogger("BlockModelManager");
    private static HashMap<String, BlockModel> models = new HashMap<>();

    public static void loadModels() throws FileNotFoundException {
        logger.info("Loading block models...");
        File[] files = ResourceUtil.getResourceFolderFiles("assets/models/block/");
        if (files == null)
            return;
        for (File file : files) {
            if (!file.getName().endsWith(".json")) {
                continue;
            }
            logger.info("Loading model: " + file.getName().replace(".json", ""));
        }
    }
}
