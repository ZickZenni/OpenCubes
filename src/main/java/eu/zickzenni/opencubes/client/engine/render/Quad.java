package eu.zickzenni.opencubes.client.engine.render;

import eu.zickzenni.opencubes.client.engine.mesh.Face;

import java.awt.*;

public class Quad {
    private static final int[] indices = new int[]{0, 1, 3, 3, 1, 2,};

    public static Face createQuad(float x1, float y1, float z, float x2, float y2, int color) {
        float[] positions = new float[] {
                x1, y1, -z,
                x1, y2, -z,
                x2, y2, -z,
                x2, y1, -z,
        };
        boolean hasAlpha = color > 16777215;
        Color convertedColor = new Color(color, hasAlpha);
        float[] colors = new float[]{
                convertedColor.getRed() / 255f,convertedColor.getGreen() / 255f,convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
                convertedColor.getRed() / 255f,convertedColor.getGreen() / 255f,convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
                convertedColor.getRed() / 255f,convertedColor.getGreen() / 255f,convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
                convertedColor.getRed() / 255f,convertedColor.getGreen() / 255f,convertedColor.getBlue() / 255f, convertedColor.getAlpha() / 255f,
        };
        return new Face(positions, colors, hasAlpha, indices);
    }
}
