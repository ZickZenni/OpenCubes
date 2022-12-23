package eu.zickzenni.opencubes.client.shader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public final class ShaderManager {
    private ShaderManager() {}

    private static final Logger logger = LogManager.getLogger("ShaderManager");
    private static HashMap<String, Shader> shaders = new HashMap<>();

    public static Shader registerShader(String name, Shader shader) {
        if (!shaders.containsKey(name)) {
            logger.info("Registering shader: " + name);
            shaders.put(name, shader);
            return shader;
        }
        return null;
    }

    public static Shader getShader(String name) {
        return shaders.getOrDefault(name, null);
    }
}