package eu.zickzenni.opencubes.client.engine.shader;

public class DefaultShader extends ShaderProgram {
    public DefaultShader(String vertexFile, String fragmentFile) throws Exception {
        createVertexShader(loadResource("/assets/shaders/" + vertexFile));
        createFragmentShader(loadResource("/assets/shaders/" + fragmentFile));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("texture_sampler");
    }
}