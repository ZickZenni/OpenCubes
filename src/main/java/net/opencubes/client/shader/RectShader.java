package net.opencubes.client.shader;

import net.opencubes.util.ResourceUtil;

public class RectShader extends Shader {
    public RectShader() throws Exception {
        super("rect");
        createVertexShader(ResourceUtil.loadResource("/assets/shaders/rect.vert"));
        createFragmentShader(ResourceUtil.loadResource("/assets/shaders/rect.frag"));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("blit");
        createUniform("texture_sampler");
    }
}