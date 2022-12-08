package eu.zickzenni.opencubes.client.engine.rect;

import eu.zickzenni.opencubes.OpenCubes;
import eu.zickzenni.opencubes.client.engine.shader.ShaderManager;
import eu.zickzenni.opencubes.client.engine.shader.ShaderProgram;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Rect {
    private static final int vertexCount = 6;
    private static final int[] indices = new int[]{0, 1, 3, 3, 1, 2,};

    public static void fill(int x1, int y1, int x2, int y2, int color) {
        int vaoId;
        List<Integer> vboIdList;

        float windowWidth = OpenCubes.getInstance().getWindow().getWidth();
        float windowHeight = OpenCubes.getInstance().getWindow().getHeight();

        float xSize = 2 / windowWidth;
        float ySize = 2 / windowHeight;

        float[] positions = new float[] {
                x1 * xSize - 1, -y1 * ySize + 1, 0f,
                x2 * xSize - 1, -y1 * ySize + 1, 0f,
                x2 * xSize - 1, -y2 * ySize + 1, 0f,
                x1 * xSize - 1, -y2 * ySize + 1, 0f,
        };

        FloatBuffer posBuffer = null;
        FloatBuffer colorBuffer = null;
        IntBuffer indicesBuffer = null;

        try {
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

            Color convertedColor = new Color(color, color > 16777215);
            float[] colors = new float[]{
                    convertedColor.getRed() / 255f,convertedColor.getGreen() / 255f,convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
                    convertedColor.getRed() / 255f,convertedColor.getGreen() / 255f,convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
                    convertedColor.getRed() / 255f,convertedColor.getGreen() / 255f,convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
                    convertedColor.getRed() / 255f,convertedColor.getGreen() / 255f,convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
            };

            // Colors VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            colorBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorBuffer.put(colors).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STREAM_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STREAM_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }

        /* RENDER */

        ShaderProgram shader = ShaderManager.getShader("gui");
        shader.bind();

        GL11.glDisable(GL11.GL_CULL_FACE);

        // Draw the mesh
        glBindVertexArray(vaoId);

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);

        // Restore state
        glBindVertexArray(0);

        shader.unbind();

        /* RENDER */

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
}
