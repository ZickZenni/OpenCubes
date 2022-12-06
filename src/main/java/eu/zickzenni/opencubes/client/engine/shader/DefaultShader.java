package eu.zickzenni.opencubes.client.engine.shader;

public class DefaultShader extends ShaderProgram {
    public DefaultShader(String vertexFile, String fragmentFile) throws Exception {
        createVertexShader(loadResource("/shaders/" + vertexFile));
        createFragmentShader(loadResource("/shaders/" + fragmentFile));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("texture_sampler");
    }
}