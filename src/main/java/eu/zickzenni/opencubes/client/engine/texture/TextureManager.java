package eu.zickzenni.opencubes.client.engine.texture;

import org.joml.Vector2f;

public final class TextureManager {
    private TextureManager() {}

    private static Texture atlas;
    private static Texture font;
    private static Texture logo;
    private static Texture background;
    private static Texture gui;

    public static final float TEXTURE_SIZE = 1 / 16f;

    public static void loadTextures() {
        atlas = new Texture("/assets/textures/terrain.png", true);
        font = new Texture("/assets/textures/gui/font.png", true);
        logo = new Texture("/assets/textures/gui/logo.png", true);
        background = new Texture("/assets/textures/gui/background.png", true);
        gui = new Texture("/assets/textures/gui/gui.png", true);
    }

    public static Vector2f getPosition(int id) {
        if (id <= 0)
            return new Vector2f(-TEXTURE_SIZE,-TEXTURE_SIZE);

        if (id <= 16) {
            return new Vector2f(TEXTURE_SIZE * id - TEXTURE_SIZE, 0);
        }
        int row = id / 16;
        float x = id / 16f - row - TEXTURE_SIZE;
        float y = TEXTURE_SIZE * row;
        return new Vector2f(x, y);
    }

    public static Texture getAtlas() {
        return atlas;
    }

    public static Texture getFont() {
        return font;
    }

    public static Texture getLogo() {
        return logo;
    }

    public static Texture getBackground() {
        return background;
    }
}