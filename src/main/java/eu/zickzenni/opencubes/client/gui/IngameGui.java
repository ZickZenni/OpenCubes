package eu.zickzenni.opencubes.client.gui;

import eu.zickzenni.opencubes.client.OpenCubes;
import eu.zickzenni.opencubes.client.font.FontRenderer;
import eu.zickzenni.opencubes.client.render.Rect;
import eu.zickzenni.opencubes.client.util.GameSettings;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class IngameGui extends AbstractGui {
    private static final int CROSSHAIR_SIZE = 12;

    @Override
    public void update(float interval) {
        if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyPressed(GLFW.GLFW_KEY_F1)) {
            OpenCubes.getInstance().screenshotMode = !OpenCubes.getInstance().screenshotMode;
        }
        if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyPressed(GLFW.GLFW_KEY_F3)) {
            OpenCubes.getInstance().debugMenu = !OpenCubes.getInstance().debugMenu;
        }
    }

    @Override
    public void render(int width, int height) {
        if (!OpenCubes.getInstance().screenshotMode) {
            renderCrosshair(width, height);
        }

        if (OpenCubes.getInstance().debugMenu && !OpenCubes.getInstance().screenshotMode) {
            int fontHeight = FontRenderer.getFontHeight() + 5;

            // https://minecraft.fandom.com/wiki/Debug_screen

            renderDebugLine("OpenCubes " + OpenCubes.VERSION, 6, 6, 0xFFFFFF);
            renderDebugLine(OpenCubes.getInstance().getFps() + " fps T: " + GameSettings.fps + " " + (GameSettings.vSync ? "vsync": ""), 6, 6 + fontHeight, 0xFFFFFF);
            renderDebugLine(OpenCubes.getInstance().getTps() + " tps", 6, 6 + fontHeight * 2, 0xFFFFFF);
            renderDebugLine("opencubes:" + OpenCubes.getInstance().getPlayer().getDimension().getName(), 6, 6 + fontHeight * 3, 0xFFFFFF);

            Vector3f playerPosition = OpenCubes.getInstance().getPlayer().getPosition();

            renderDebugLine("XYZ:" + formatNumber(playerPosition.x) + " / " + formatNumber(playerPosition.y) + " / " + formatNumber(playerPosition.z), 6, 6 + fontHeight * 5, 0xFFFFFF);

            String javaVersion = Runtime.version().toString();
            renderDebugLineRTL("Java: " + javaVersion, width - 6, 6, 0xFFFFFF);

            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            long usedMemory = totalMemory - freeMemory;
            int percent = (int) ((1f / totalMemory) * usedMemory * 100);
            renderDebugLineRTL("Mem: " + percent + "% " + usedMemory / 1000000 + "/" + totalMemory / 1000000 + "MB", width - 6, 6 + fontHeight, 0xFFFFFF);
        }
    }

    private String formatNumber(float number) {
        return String.format(java.util.Locale.US, "%.3f", number);
    }

    private void renderDebugLineRTL(String text, int x, int y, int color) {
        Rect.fill(x - 6 - FontRenderer.getStringWidth(text), y - 2, x + 2, y + 2 + FontRenderer.getFontHeight(), 1,  0x55FFFFFF);
        FontRenderer.renderString(text, x - FontRenderer.getStringWidth(text) - 5, y, 1, color);
    }

    private void renderDebugLine(String text, int x, int y, int color) {
        Rect.fill(x - 2, y - 2, x + 2 + FontRenderer.getStringWidth(text) + 4, y + 2 + FontRenderer.getFontHeight(), 1,  0x55FFFFFF);
        FontRenderer.renderString(text, x, y, 1, color);
    }

    private void renderCrosshair(int width, int height) {
        Rect.fill(width / 2 - (CROSSHAIR_SIZE / 2),
                height / 2 - (CROSSHAIR_SIZE / 2),
                width / 2 + (CROSSHAIR_SIZE / 2),
                height / 2 + (CROSSHAIR_SIZE / 2),
                1, 0xFFFFFFFF);

        /*Texture gui = TextureManager.getGui();
        float perPixelX = 1f / gui.getWidth();
        float perPixelY = 1f / gui.getHeight();

        Rect.blit(width / 2 - (CROSSHAIR_SIZE / 2),
                height / 2 - (CROSSHAIR_SIZE / 2),
                width / 2 + (CROSSHAIR_SIZE / 2),
                height / 2 + (CROSSHAIR_SIZE / 2),
                1,
                perPixelX * 15,
                perPixelY * 15,
                perPixelX * (gui.getWidth() - 16),
                0,
                gui,
                0xFFFFFFFF);*/
    }
}