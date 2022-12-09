package eu.zickzenni.opencubes.client.engine.render;

import eu.zickzenni.opencubes.OpenCubes;
import eu.zickzenni.opencubes.client.engine.mesh.Face;
import eu.zickzenni.opencubes.client.engine.mesh.Mesh;
import eu.zickzenni.opencubes.client.engine.shader.ShaderManager;
import eu.zickzenni.opencubes.client.engine.shader.ShaderProgram;
import eu.zickzenni.opencubes.client.engine.texture.Texture;
import org.lwjgl.opengl.GL11;

public class Rect {
    public static void fill(int x1, int y1, int x2, int y2, int z, int color) {
        render(x1, y1, x2, y2, z, 0, 0,  0, 0, null, color);
    }

    public static void blit(int x1, int y1, int x2, int y2, int z, Texture texture) {
        render(x1, y1, x2, y2, z, texture.getWidth(), texture.getHeight(), 0, 0,  texture,0xFFFFFF);
    }

    public static void blit(int x1, int y1, int x2, int y2, int z, float width, float height, Texture texture) {
        render(x1, y1, x2, y2, z, width, height, 0, 0,  texture,0xFFFFFF);
    }

    public static void blit(int x1, int y1, int x2, int y2, int z, float width, float height, Texture texture, int color) {
        render(x1, y1, x2, y2, z, width, height, 0, 0, texture,color);
    }

    public static void blit(int x1, int y1, int x2, int y2, int z, float width, float height, float uvX, float uvY, Texture texture, int color) {
        render(x1, y1, x2, y2, z, width, height, uvX, uvY, texture,color);
    }

    private static void render(int x1, int y1, int x2, int y2, int z, float width, float height, float uvX, float uvY, Texture texture, int color) {
        float windowWidth = OpenCubes.getInstance().getWindow().getWidth();
        float windowHeight = OpenCubes.getInstance().getWindow().getHeight();

        float xSize = 2 / windowWidth;
        float ySize = 2 / windowHeight;

        Face face = Quad.createQuad(x1 * xSize - 1, -y1 * ySize + 1, z, x2 * xSize - 1, -y2 * ySize + 1, color);
        if (texture != null) {
            float[] textCoords = new float[]{
                    uvX, uvY,
                    uvX, height / texture.getHeight(),
                    uvX + width / texture.getWidth(), uvY + height / texture.getHeight(),
                    uvX + width / texture.getWidth(), uvY,
            };
            face.setTextureCoords(textCoords);
        }

        Mesh mesh = new Mesh(face, texture);

        /* RENDER */

        ShaderProgram shader = ShaderManager.getShader("gui");
        shader.bind();
        shader.setUniform("blit", texture != null ? 1 : 0);
        shader.setUniform("texture_sampler", 0);

        GL11.glDisable(GL11.GL_CULL_FACE);

        mesh.render();

        shader.unbind();

        /* RENDER */

        mesh.cleanup();
    }
}
