package net.opencubes.client.shader;

import net.opencubes.util.ResourceUtil;

public class DefaultShader extends Shader {
    public DefaultShader() throws Exception {
        super("default");
        createVertexShader(ResourceUtil.loadResource("/assets/shaders/default.vert"));
        createFragmentShader(ResourceUtil.loadResource("/assets/shaders/default.frag"));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("texture_sampler");
    }
}