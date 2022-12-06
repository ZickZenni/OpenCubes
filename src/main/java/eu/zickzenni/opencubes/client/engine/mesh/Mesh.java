package eu.zickzenni.opencubes.client.engine.mesh;

import eu.zickzenni.opencubes.client.engine.texture.Texture;
import eu.zickzenni.opencubes.client.engine.util.Converter;
import org.joml.Vector3f;
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
    private int vertexCount;
    private Texture texture;

    private Face[] faces;
    private float[] positions;
    private float[] textCoords;

    private float[] colors;
    private boolean hasColors = true;

    private int[] indices;
    private boolean created = false;

    public Mesh(Face[] faces, Texture texture) {
        this.faces = faces;
        ArrayList<Float> pos = new ArrayList<>();
        ArrayList<Float> tex = new ArrayList<>();
        ArrayList<Float> col = new ArrayList<>();
        ArrayList<Integer> ind = new ArrayList<>();

        for (Face face : faces) {
            for (Vector3f position : face.getPositions()) {
                pos.add(position.x);
                pos.add(position.y);
                pos.add(position.z);
            }

            for (int x = 0; x < face.getTextureCoords().length; x++) {
                tex.add(face.getTextureCoords()[x]);
            }

            for (float color : face.getColors()) {
                col.add(color);
            }

            for (int y = 0; y < face.getIndices().length; y++) {
                ind.add(face.getIndices()[y]);
            }
        }

        this.positions = Converter.convertFloat(pos);
        this.textCoords = Converter.convertFloat(tex);
        this.indices = Converter.convertInt(ind);
        this.colors = Converter.convertFloat(col);
        create(texture);
    }

    public Mesh(float[] positions, float[] textCoords, float[] colors, int[] indices, Texture texture, boolean createLater) {
        this.positions = positions;
        this.textCoords = textCoords;
        this.indices = indices;
        this.colors = colors;
        if (!createLater) {
            create(texture);
        }
    }

    public Mesh(float[] positions, float[] textCoords, int[] indices, Texture texture, boolean createLater) {
        this.positions = positions;
        this.textCoords = textCoords;
        this.indices = indices;
        this.colors = new float[0];
        this.hasColors = false;
        if (!createLater) {
            create(texture);
        }
    }

    private void create(Texture texture) {
        if (created) {
            return;
        }

        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer colorBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            this.texture = texture;
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            if (hasColors) {
                // Colors VBO
                vboId = glGenBuffers();
                vboIdList.add(vboId);
                colorBuffer = MemoryUtil.memAllocFloat(colors.length);
                colorBuffer.put(colors).flip();
                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
                glEnableVertexAttribArray(2);
                glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
            }

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
        // Activate firs texture bank
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        texture.bind();

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

    public Face[] getFaces() {
        return faces;
    }

    public float[] getPositions() {
        return positions;
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
}
