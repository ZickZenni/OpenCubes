package eu.zickzenni.opencubes.client.engine.render;

import eu.zickzenni.opencubes.OpenCubes;
import eu.zickzenni.opencubes.client.engine.mesh.Face;
import eu.zickzenni.opencubes.client.engine.mesh.Mesh;
import eu.zickzenni.opencubes.client.engine.mesh.MeshBuilder;
import eu.zickzenni.opencubes.client.engine.shader.ShaderManager;
import eu.zickzenni.opencubes.client.engine.shader.ShaderProgram;
import eu.zickzenni.opencubes.client.engine.texture.Texture;
import eu.zickzenni.opencubes.client.engine.texture.TextureManager;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

public class FontRenderer {
    public static void renderString(String text, int x, int y, int z, int color) {
        renderString(text, x, y, z, TextureManager.getFont(), color);
    }

    private static void renderString(String text, int x, int y, int z, Texture texture, int color) {
        float windowWidth = OpenCubes.getInstance().getWindow().getWidth();
        float windowHeight = OpenCubes.getInstance().getWindow().getHeight();

        float width = texture.getWidth() * 16;
        float height = texture.getWidth() * 16;

        float xSize = 2 / windowWidth;
        float ySize = 2 / windowHeight;

        float fontSize = 32;

        if (text.length() == 0) {
            return;
        }

        MeshBuilder builder = new MeshBuilder();

        float xF = 0;
        for (int i1 = 0; i1 < text.length(); i1++) {
            int k1 = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~\u2302\307\374\351\342\344\340\345\347\352\353\350\357\356\354\304\305\311\346\306\364\366\362\373\371\377\326\334\370\243\330\327\u0192\341\355\363\372\361\321\252\272\277\256\254\275\274\241\253\273".indexOf(text.charAt(i1));
            if (k1 >= 0) {
                int pos = (k1 + 32);
                float x2 = 32 + xF + x;
                float y2 = 32 + y;
                Face face = Quad.createQuad((x + xF) * xSize - 1, -y * ySize + 1, z, x2 * xSize - 1, -y2 * ySize + 1, color);

                Vector2f position = getPosition(pos, texture);
                float uvX = position.x;
                float uvY = position.y;
                float[] textCoords = new float[]{
                        uvX / texture.getWidth(), uvY / texture.getHeight(),
                        uvX / texture.getWidth(), uvY / texture.getHeight() + texture.getHeight() / height,
                        uvX / texture.getWidth() + texture.getWidth() / width, uvY / texture.getHeight() + texture.getHeight() / height,
                        uvX / texture.getWidth() + texture.getWidth() / width, uvY / texture.getHeight(),
                };
                face.setTextureCoords(textCoords);

                xF += 32;
                builder.addFace(face, 0, 0, 0);
            }
        }

        Mesh mesh = builder.build(texture);

        /* RENDER */

        ShaderProgram shader = ShaderManager.getShader("gui");
        shader.bind();
        shader.setUniform("blit", 1);
        shader.setUniform("texture_sampler", 0);

        GL11.glDisable(GL11.GL_CULL_FACE);

        mesh.render();

        shader.unbind();

        /* RENDER */

        mesh.cleanup();
    }

    private static Vector2f getPosition(int id, Texture texture) {
        float TEXTURE_SIZE = texture.getWidth() / 16f;

        if (id <= 16) {
            return new Vector2f(TEXTURE_SIZE * id, 0);
        }
        int row = id / 16;
        return new Vector2f(TEXTURE_SIZE * (id % 16), TEXTURE_SIZE * row);
    }
}
