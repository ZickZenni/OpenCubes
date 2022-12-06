package eu.zickzenni.opencubes.client.engine.shader;

import java.util.HashMap;

public final class ShaderManager {
    private ShaderManager() {}

    private static HashMap<String, ShaderProgram> shaders = new HashMap<>();

    public static ShaderProgram registerShader(String name, ShaderProgram shader) {
        if (!shaders.containsKey(name)) {
            shaders.put(name, shader);
            return shader;
        }
        return null;
    }

    public static ShaderProgram getShader(String name) {
        return shaders.getOrDefault(name, null);
    }
}