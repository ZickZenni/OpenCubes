package eu.zickzenni.opencubes.client.shader;

public class DefaultShader extends Shader {
    public DefaultShader(String name, String vertexFile, String fragmentFile) throws Exception {
        super(name);
        createVertexShader(loadResource("/assets/shaders/" + vertexFile));
        createFragmentShader(loadResource("/assets/shaders/" + fragmentFile));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
        createUniform("texture_sampler");
    }
}