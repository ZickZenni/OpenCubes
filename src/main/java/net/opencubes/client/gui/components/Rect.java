package net.opencubes.client.gui.components;

import net.opencubes.client.OpenCubes;
import net.opencubes.client.renderer.GameRenderer;
import net.opencubes.client.shader.Shader;
import net.opencubes.client.systems.RenderSystem;
import net.opencubes.client.vertex.Face;
import net.opencubes.client.vertex.Mesh;
import net.opencubes.client.vertex.Model;
import net.opencubes.world.physics.Vec3;

import java.awt.*;

public final class Rect {
    private static float z = 1;

    private Rect() {
    }

    public static void drawRect(int x, int y, int width, int height, int color) {
        drawRect(x, y, width, height, 0, 0, 1, 1, color);
    }

    public static void drawRect(int x, int y, int width, int height, float uvX, float uvY, float uvWidth, float uvHeight, int color) {
        Face face = createFace(width, height, z += 0.01f, uvX, uvY, uvWidth, uvHeight);
        boolean hasAlpha = color > 16777215;
        Color convertedColor = new Color(color, hasAlpha);
        float[] colors = new float[]{
                convertedColor.getRed() / 255f, convertedColor.getGreen() / 255f, convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
                convertedColor.getRed() / 255f, convertedColor.getGreen() / 255f, convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
                convertedColor.getRed() / 255f, convertedColor.getGreen() / 255f, convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
                convertedColor.getRed() / 255f, convertedColor.getGreen() / 255f, convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
        };
        face.setColors(colors);

        Model model = new Model(new Mesh(face), 1, new Vec3(x, y + height, z));

        Shader shader = GameRenderer.RECT_SHADER;
        shader.bind();
        shader.setUniform("blit", OpenCubes.getInstance().gameRenderer.getBoundTexture() != null ? 1 : 0);
        shader.setUniform("texture_sampler", 0);

        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();

        OpenCubes.getInstance().gameRenderer.renderModelOrthographic(model, shader);

        model.getMesh().cleanup();
    }

    private static Face createFace(float sizeX, float sizeY, float sizeZ, float uvX, float uvY, float uvWidth, float uvHeight) {
        return new Face(new float[]{
                0, sizeY, sizeZ,
                0, 0, sizeZ,
                sizeX, 0, sizeZ,
                sizeX, sizeY, sizeZ
        }, new float[]{
                uvX, uvY,
                uvX, uvY + uvHeight,
                uvX + uvWidth, uvY + uvHeight,
                uvX + uvWidth, uvY,
        }, new float[]{
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
        }, new int[]{0, 1, 3, 3, 1, 2,});
    }

    public static void reset() {
        z = 1;
    }
}