package eu.zickzenni.opencubes.client.engine.mesh;

import org.joml.Vector3f;

public class Face {
    private Vector3f[] positions;
    private float[] textureCoords;
    private float[] colors;
    private int[] indices;

    public Face(Vector3f[] positions, float[] textureCoords, float[] colors, int[] indices) {
        this.positions = positions;
        this.textureCoords = textureCoords;
        this.colors = colors;
        this.indices = indices;
    }

    public Vector3f[] getPositions() {
        return positions;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

    public float[] getColors() {
        return colors;
    }

    public void setColors(float[] colors) {
        this.colors = colors;
    }

    public int[] getIndices() {
        return indices;
    }
}