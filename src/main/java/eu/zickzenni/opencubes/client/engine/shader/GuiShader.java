package eu.zickzenni.opencubes.client.engine.shader;

public class GuiShader extends ShaderProgram {
    public GuiShader(String vertexFile, String fragmentFile) throws Exception {
        createVertexShader(loadResource("/shaders/" + vertexFile));
        createFragmentShader(loadResource("/shaders/" + fragmentFile));
        link();

        createUniform("projectionMatrix");
        createUniform("texture_sampler");
    }
}