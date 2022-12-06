package eu.zickzenni.opencubes.client.engine.mesh;

import eu.zickzenni.opencubes.client.engine.texture.Texture;
import eu.zickzenni.opencubes.client.engine.texture.TextureManager;
import eu.zickzenni.opencubes.client.engine.util.Converter;
import org.joml.Vector3f;

import java.util.ArrayList;

public class MeshBuilder {
    private ArrayList<Vector3f> vertices = new ArrayList<>();
    private ArrayList<Float> texCoords = new ArrayList<>();
    private ArrayList<Float> colors = new ArrayList<>();
    private ArrayList<Integer> indices = new ArrayList<>();

    private int indicesOffset = 0;

    public void addFace(Face face) {
        addFace(face, 0, 0, 0);
    }

    public void addFace(Face face, float offsetX, float offsetY, float offsetZ) {
        if (face == null) {
            return;
        }
        for (Vector3f position : face.getPositions()) {
            vertices.add(position.add(offsetX, offsetY, offsetZ));
        }
        for (int i = 0; i < face.getTextureCoords().length; i++) {
            texCoords.add(face.getTextureCoords()[i]);
        }
        for (float color : face.getColors()) {
            colors.add(color);
        }

        for (int i = 0; i < face.getIndices().length; i++) {
            indices.add(indicesOffset + face.getIndices()[i]);
        }
        indicesOffset += face.getPositions().length;
    }

    public Mesh build() {
        return build(TextureManager.getAtlas());
    }

    public Mesh build(Texture texture) {
        Mesh mesh = new Mesh(Converter.convertVector3f(vertices), Converter.convertFloat(texCoords), Converter.convertFloat(colors), Converter.convertInt(indices), texture, false);
        clear();
        return mesh;
    }

    public void clear() {
        vertices.clear();
        texCoords.clear();
        colors.clear();
        indices.clear();
    }

    public void copy(MeshBuilder builder) {
        this.vertices = builder.vertices;
        this.texCoords = builder.texCoords;
        this.colors = builder.colors;
        this.indices = builder.indices;
    }
}
