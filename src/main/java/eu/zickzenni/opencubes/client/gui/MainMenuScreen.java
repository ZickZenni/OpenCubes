package eu.zickzenni.opencubes.client.gui;

import eu.zickzenni.opencubes.OpenCubes;
import eu.zickzenni.opencubes.client.engine.render.Rect;
import eu.zickzenni.opencubes.client.engine.texture.TextureManager;
import org.lwjgl.glfw.GLFW;

public final class MainMenuScreen  {
    private static boolean displayLogo = true;
    private static float logoTime = 2.25f;

    public static void update(float interval) {
        if (displayLogo) {
            if (logoTime > 0) {
                logoTime -= interval;
                if (logoTime <= 0) {
                    displayLogo = false;
                }
            }
        } else {
           if (OpenCubes.getInstance().getWindow().isKeyPressed(GLFW.GLFW_KEY_F)) {
               OpenCubes.getInstance().generateWorld();
           }
        }
    }

    public static void render() {
        int logoX = OpenCubes.getInstance().getWindow().getWidth() / 2 - TextureManager.getLogo().getWidth() / 2;

        if (displayLogo) {
            Rect.fill(0,0,OpenCubes.getInstance().getWindow().getWidth(),OpenCubes.getInstance().getWindow().getHeight(), 0, 0x0D1117);

            int y = OpenCubes.getInstance().getWindow().getHeight() / 2 - TextureManager.getLogo().getHeight() / 2;
            Rect.blit(logoX,y,TextureManager.getLogo().getWidth() + logoX, TextureManager.getLogo().getHeight() + y, 1, TextureManager.getLogo());
            return;
        }
        renderBackground();

        int y = 60;
        Rect.blit(logoX,y,TextureManager.getLogo().getWidth() + logoX, TextureManager.getLogo().getHeight() + y, 1, TextureManager.getLogo());
    }

    private static void renderBackground() {
        int width = OpenCubes.getInstance().getWindow().getWidth();
        int height = OpenCubes.getInstance().getWindow().getHeight();

        int biggest = Math.max(width, height);
        float size = biggest / 6f;

        Rect.blit(0,0,biggest, biggest, 0, size, size, TextureManager.getBackground(), 0x707070);
    }
}