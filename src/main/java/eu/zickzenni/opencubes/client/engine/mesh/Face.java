package eu.zickzenni.opencubes.client.engine.mesh;

public class Face {
    private final float[] vertices;
    private float[] textureCoords;
    private float[] colors;
    private boolean hasAlpha = false;
    private final int[] indices;

    public Face(float[] vertices, float[] colors, int[] indices) {
        this(vertices,colors, false, indices);
    }

    public Face(float[] vertices, float[] colors, boolean hasAlpha, int[] indices) {
        if (vertices.length % 3 != 0) {
            throw new IllegalArgumentException("Each vertex should have 3 position (xyz)!");
        }
        this.vertices = vertices;
        this.textureCoords = new float[0];
        this.colors = colors;
        this.hasAlpha = hasAlpha;
        this.indices = indices;
    }

    public Face(float[] vertices, float[] textureCoords, float[] colors, int[] indices) {
        if (vertices.length % 3 != 0) {
            throw new IllegalArgumentException("Each vertex should have 3 position (xyz)!");
        }
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.colors = colors;
        this.indices = indices;
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

    public void setTextureCoords(float[] textureCoords) {
        this.textureCoords = textureCoords;
    }

    public float[] getColors() {
        return colors;
    }

    public void setColors(float[] colors) {
        this.colors = colors;
    }

    public boolean hasAlpha() {
        return hasAlpha;
    }

    public int[] getIndices() {
        return indices;
    }
}