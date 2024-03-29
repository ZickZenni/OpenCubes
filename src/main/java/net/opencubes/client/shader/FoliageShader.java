package net.opencubes.client.shader;

import net.opencubes.util.ResourceUtil;

public class FoliageShader extends Shader {
    public FoliageShader() throws Exception {
        super("foliage", true);
        createVertexShader(ResourceUtil.loadResource("/assets/shaders/foliage.vert"));
        createFragmentShader(ResourceUtil.loadResource("/assets/shaders/chunk.frag"));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("texture_sampler");
    }
}