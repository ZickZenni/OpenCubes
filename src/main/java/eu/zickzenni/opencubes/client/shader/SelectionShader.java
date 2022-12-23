package eu.zickzenni.opencubes.client.shader;

public class SelectionShader extends Shader {
    public SelectionShader(String name, String vertexFile, String fragmentFile) throws Exception {
        super(name);
        createVertexShader(loadResource("/assets/shaders/" + vertexFile));
        createFragmentShader(loadResource("/assets/shaders/" + fragmentFile));
        link();

        createUniform("projectionMatrix");
        createUniform("modelViewMatrix");
    }
}
