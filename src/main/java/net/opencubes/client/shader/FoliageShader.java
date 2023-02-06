package net.opencubes.client.shader;

import net.opencubes.client.vertex.Model;
import net.opencubes.util.ResourceUtil;
import org.lwjgl.glfw.GLFW;

public class FoliageShader extends Shader {
    public FoliageShader() throws Exception {
        super("foliage");
        createVertexShader(ResourceUtil.loadResource("/assets/shaders/foliage.vert"));
        createFragmentShader(ResourceUtil.loadResource("/assets/shaders/default.frag"));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("texture_sampler");
        createUniform("time");
    }

    @Override
    public void bind(Model model) {
        super.bind(model);
        setUniform("time", (float) GLFW.glfwGetTime());
    }
}