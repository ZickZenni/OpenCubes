package net.opencubes.client.shader;

import net.opencubes.util.ResourceUtil;

public class ChunkShader extends Shader {
    public ChunkShader() throws Exception {
        super("chunk");
        createVertexShader(ResourceUtil.loadResource("/assets/shaders/chunk.vert"));
        createFragmentShader(ResourceUtil.loadResource("/assets/shaders/chunk.frag"));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("texture_sampler");
        createUniform("texture_offset");
    }
}