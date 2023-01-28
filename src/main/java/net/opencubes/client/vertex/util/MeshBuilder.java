package net.opencubes.client.vertex.util;

import net.opencubes.client.vertex.Face;
import net.opencubes.client.vertex.Mesh;
import net.opencubes.util.ConverterHelper;

import java.util.ArrayList;

public class MeshBuilder {
    private static class MeshBuilderFace {
        private Face face;
        private float offsetX;
        private float offsetY;
        private float offsetZ;

        public MeshBuilderFace(Face face, float offsetX, float offsetY, float offsetZ) {
            this.face = face;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
        }
    }

    private ArrayList<MeshBuilderFace> faces = new ArrayList<>();

    public MeshBuilder addFace(Face face) {
        return addFace(face, 0, 0, 0);
    }

    public MeshBuilder addFace(Face face, float offsetX, float offsetY, float offsetZ) {
        if (face == null) {
            return this;
        }
        faces.add(new MeshBuilderFace(face, offsetX, offsetY, offsetZ));
        return this;
    }

    public Mesh build() {
        ArrayList<Float> vertices = new ArrayList<>();
        ArrayList<Float> tex = new ArrayList<>();
        ArrayList<Float> col = new ArrayList<>();
        ArrayList<Integer> ind = new ArrayList<>();

        int indicesOffset = 0;
        for (MeshBuilderFace mFace : faces) {
            Face face = mFace.face;
            for (int i = 0; i < face.getVertices().length / 3; i++) {
                vertices.add(face.getVertices()[i * 3] + mFace.offsetX);
                vertices.add(face.getVertices()[i * 3 + 1] + mFace.offsetY);
                vertices.add(face.getVertices()[i * 3 + 2] + mFace.offsetZ);
            }

            for (float textureCoord : face.getTextureCoords()) {
                tex.add(textureCoord);
            }
            for (float color : face.getColors()) {
                col.add(color);
            }
            for (int index : face.getIndices()) {
                ind.add(indicesOffset + index);
            }
            indicesOffset += face.getVertices().length / 3;
        }
        Mesh mesh = new Mesh(ConverterHelper.convertFloat(vertices), ConverterHelper.convertFloat(tex), ConverterHelper.convertFloat(col), ConverterHelper.convertInt(ind));
        clear();
        return mesh;
    }

    public void clear() {
        faces.clear();
    }

    public void copy(MeshBuilder builder) {
        this.faces = builder.faces;
    }

    public ArrayList<MeshBuilderFace> getFaces() {
        return faces;
    }
}