package eu.zickzenni.opencubes.client.gui;

import eu.zickzenni.opencubes.client.OpenCubes;
import eu.zickzenni.opencubes.client.font.FontRenderer;
import eu.zickzenni.opencubes.client.render.Rect;
import eu.zickzenni.opencubes.client.texture.TextureManager;
import org.lwjgl.glfw.GLFW;

public class MainMenuGui extends AbstractGui {
    private static boolean displayLogo = true;
    private static float logoTime = 2.25f;

    @Override
    public void update(float interval) {
        if (displayLogo) {
            if (logoTime > 0) {
                logoTime -= interval;
                if (logoTime <= 0) {
                    displayLogo = false;
                }
            }
        } else {
           if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyPressed(GLFW.GLFW_KEY_F)) {
               OpenCubes.getInstance().generateWorld();
           }
        }
    }

    @Override
    public void render(int width, int height) {
        int logoX = width / 2 - TextureManager.getLogo().getWidth() / 2;

        if (displayLogo) {
            Rect.fill(0,0,width,height, 0, 0x373363);

            int y = height / 2 - TextureManager.getLogo().getHeight() / 2;
            Rect.blit(logoX,y,TextureManager.getLogo().getWidth() + logoX, TextureManager.getLogo().getHeight() + y, 1, TextureManager.getLogo());
            return;
        }
        renderBackground(width, height);

        int y = 60;
        Rect.blit(logoX,y,TextureManager.getLogo().getWidth() + logoX, TextureManager.getLogo().getHeight() + y, 1, TextureManager.getLogo());

        String versionText = "OpenCubes " + OpenCubes.VERSION;
        FontRenderer.renderString(versionText, width - FontRenderer.getStringWidth(versionText), height - FontRenderer.getFontHeight(), 1, 0xFFFFFF);

        FontRenderer.renderString(OpenCubes.getInstance().getSession().getUsername(), 5, height - FontRenderer.getFontHeight(), 1, 0xFFFFFF);
    }

    public static void renderBackground(int width, int height) {
        int biggest = Math.max(width, height);
        float size = biggest / 6f;

        Rect.blit(0,0,biggest, biggest, 0, size, size, TextureManager.getBackground(), 0x707070);
    }
}