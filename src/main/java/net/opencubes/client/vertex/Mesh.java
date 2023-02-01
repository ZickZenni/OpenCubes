package net.opencubes.client.vertex;

import net.opencubes.util.ConverterHelper;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private int vaoId;
    private List<Integer> vboIdList;

    private final int vertexCount;
    private float[] vertices;
    private float[] textCoords;
    private float[] colors;
    private int[] indices;

    private boolean created = false;

    public Mesh(Face face) {
        this(new Face[]{face});
    }

    public Mesh(Face[] faces) {
        ArrayList<Float> pos = new ArrayList<>();
        ArrayList<Float> tex = new ArrayList<>();
        ArrayList<Float> col = new ArrayList<>();
        ArrayList<Integer> ind = new ArrayList<>();

        for (Face face : faces) {
            for (float position : face.getVertices()) {
                pos.add(position);
            }

            for (float textureCoord : face.getTextureCoords()) {
                tex.add(textureCoord);
            }

            for (float color : face.getColors()) {
                col.add(color);
            }

            for (int index : face.getIndices()) {
                ind.add(index);
            }
        }

        this.vertices = ConverterHelper.convertFloat(pos);
        this.textCoords = ConverterHelper.convertFloat(tex);
        this.indices = ConverterHelper.convertInt(ind);
        this.vertexCount = vertices.length;
        this.colors = ConverterHelper.convertFloat(col);
        create();
    }

    public Mesh(float[] vertices, float[] textCoords, float[] colors, int[] indices) {
        this.vertices = vertices;
        this.textCoords = textCoords;
        this.indices = indices;
        this.vertexCount = vertices.length;
        this.colors = colors;
        create();
    }

    private void create() {
        if (created) {
            return;
        }

        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer colorBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vboIdList = new ArrayList<>();

            vaoId = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoId);

            // Position VBO
            int vboId = GL30.glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(vertices.length);
            posBuffer.put(vertices).flip();
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, posBuffer, GL30.GL_STATIC_DRAW);
            GL30.glEnableVertexAttribArray(0);
            GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);

            if (textCoords.length != 0) {
                // Texture coordinates VBO
                vboId = GL30.glGenBuffers();
                vboIdList.add(vboId);
                textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
                textCoordsBuffer.put(textCoords).flip();
                GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
                GL30.glBufferData(GL30.GL_ARRAY_BUFFER, textCoordsBuffer, GL30.GL_STATIC_DRAW);
                GL30.glEnableVertexAttribArray(1);
                GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 0, 0);
            }

            // Colors VBO
            vboId = GL30.glGenBuffers();
            vboIdList.add(vboId);
            colorBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorBuffer.put(colors).flip();
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, colorBuffer, GL30.GL_STATIC_DRAW);
            GL30.glEnableVertexAttribArray(2);
            GL30.glVertexAttribPointer(2, 4, GL30.GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = GL30.glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL30.GL_STATIC_DRAW);

            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);

            vertices = null;
            textCoords = null;
            colors = null;
            indices = null;
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
            created = true;
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void render() {
        // Draw the mesh
        GL30.glBindVertexArray(getVaoId());

        GL30.glDrawElements(GL30.GL_TRIANGLES, getVertexCount(), GL30.GL_UNSIGNED_INT, 0);

        // Restore state
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        GL30.glDisableVertexAttribArray(0);

        // Delete the VBOs
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            GL30.glDeleteBuffers(vboId);
        }

        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
    }
}
