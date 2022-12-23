package eu.zickzenni.opencubes.client.shader;

public class GuiShader extends Shader {
    public GuiShader(String vertexFile, String fragmentFile) throws Exception {
        super("gui");
        createVertexShader(loadResource("/assets/shaders/" + vertexFile));
        createFragmentShader(loadResource("/assets/shaders/" + fragmentFile));
        link();

        createUniform("blit");
        createUniform("texture_sampler");
    }
}