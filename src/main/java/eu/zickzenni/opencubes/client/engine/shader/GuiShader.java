package eu.zickzenni.opencubes.client.engine.shader;

public class GuiShader extends ShaderProgram {
    public GuiShader(String vertexFile, String fragmentFile) throws Exception {
        createVertexShader(loadResource("/assets/shaders/" + vertexFile));
        createFragmentShader(loadResource("/assets/shaders/" + fragmentFile));
        link();

        createUniform("blit");
        createUniform("texture_sampler");
    }
}