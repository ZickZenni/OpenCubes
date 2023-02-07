package net.opencubes.client.shader;

import net.opencubes.client.vertex.Model;
import net.opencubes.util.ResourceUtil;

public class FluidShader extends Shader {
    public FluidShader() throws Exception {
        super("fluid", true);
        createVertexShader(ResourceUtil.loadResource("/assets/shaders/fluid.vert"));
        createFragmentShader(ResourceUtil.loadResource("/assets/shaders/chunk.frag"));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("texture_sampler");
        createUniform("texture_offset");
        createUniform("modelPosition");
    }

    @Override
    public void bind(Model model) {
        super.bind(model);
        setUniform("modelPosition", model.getPosition());
    }
}
