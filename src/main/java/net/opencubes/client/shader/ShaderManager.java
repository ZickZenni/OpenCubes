package net.opencubes.client.shader;

import java.util.HashMap;

public class ShaderManager {
    private static HashMap<String, Shader> shaders = new HashMap<>();

    public static void init() {
        try {
            shaders.put("default", new DefaultShader());
            shaders.put("chunk", new ChunkShader());
            shaders.put("rect", new RectShader());
            shaders.put("selection", new SelectionShader());
            shaders.put("foliage", new FoliageShader());
            shaders.put("fluid", new FluidShader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Shader> getShaders() {
        return shaders;
    }

    public static Shader getShader(String name) {
        return shaders.getOrDefault(name, null);
    }
}
