package net.opencubes.client.shader;

import net.opencubes.client.vertex.Model;
import net.opencubes.util.ResourceUtil;
import org.lwjgl.glfw.GLFW;

public class FluidShader extends Shader {
    public FluidShader() throws Exception {
        super("fluid");
        createVertexShader(ResourceUtil.loadResource("/assets/shaders/fluid.vert"));
        createFragmentShader(ResourceUtil.loadResource("/assets/shaders/default.frag"));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("texture_sampler");
        createUniform("time");
        createUniform("modelPosition");
    }

    @Override
    public void bind(Model model) {
        super.bind(model);
        setUniform("time", (float) GLFW.glfwGetTime());
        setUniform("modelPosition", model.getPosition());
    }
}
