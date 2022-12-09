package eu.zickzenni.opencubes.client.engine.mesh;

import eu.zickzenni.opencubes.client.engine.texture.Texture;
import eu.zickzenni.opencubes.client.engine.util.Converter;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vaoId;
    private List<Integer> vboIdList;

    private Texture texture;

    private final int vertexCount;
    private final float[] vertices;
    private final float[] textCoords;
    private final float[] colors;
    private final int[] indices;

    private boolean created = false;

    public Mesh(Face face, Texture texture) {
        this(new Face[]{face}, texture);
    }

    public Mesh(Face[] faces, Texture texture) {
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

        this.vertices = Converter.convertFloat(pos);
        this.textCoords = Converter.convertFloat(tex);
        this.indices = Converter.convertInt(ind);
        this.vertexCount = this.indices.length;
        this.colors = Converter.convertFloat(col);
        this.texture = texture;
        create();
    }

    public Mesh(float[] vertices, float[] textCoords, float[] colors, int[] indices, Texture texture) {
        this.vertices = vertices;
        this.textCoords = textCoords;
        this.indices = indices;
        this.vertexCount = this.indices.length;
        this.colors = colors;
        this.texture = texture;
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

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(vertices.length);
            posBuffer.put(vertices).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            if (textCoords.length != 0) {
                // Texture coordinates VBO
                vboId = glGenBuffers();
                vboIdList.add(vboId);
                textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
                textCoordsBuffer.put(textCoords).flip();
                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
                glEnableVertexAttribArray(1);
                glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            }

            // Colors VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            colorBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorBuffer.put(colors).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 4, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
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
        if (texture != null) {
            // Activate firs texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            texture.bind();
        }

        // Draw the mesh
        glBindVertexArray(getVaoId());

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTextCoords() {
        return textCoords;
    }

    public float[] getColors() {
        return colors;
    }

    public int[] getIndices() {
        return indices;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}