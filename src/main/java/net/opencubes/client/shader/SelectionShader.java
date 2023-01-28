package net.opencubes.client.shader;

import net.opencubes.util.ResourceUtil;

public class SelectionShader extends Shader {
    public SelectionShader() throws Exception {
        super("selection");
        createVertexShader(ResourceUtil.loadResource("/assets/shaders/selection.vert"));
        createFragmentShader(ResourceUtil.loadResource("/assets/shaders/selection.frag"));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
    }
}
