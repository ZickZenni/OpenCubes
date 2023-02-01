package net.opencubes.client.gui.components.font;

import net.opencubes.client.OpenCubes;
import net.opencubes.client.gui.components.Rect;
import net.opencubes.client.renderer.texture.Texture;
import net.opencubes.client.systems.RenderSystem;
import org.joml.Vector2f;

public class FontRenderer {
    private static final int DEFAULT_FONT_SIZE = 27;

    public static void init(Font font) {
        FontRenderer.font = font;
    }
    private static Font font;

    public static void drawString(String text, int x, int y, int color) {
        drawString(text, x, y, font.getTexture(), color);
    }

    public static void drawCenteredString(String text, int x, int y, int color) {
        x -= (getStringWidth(text) / 2);
        drawString(text, x, y, font.getTexture(), color);
    }

    public static void drawCenteredStringWithShadow(String text, int x, int y, int color) {
        x -= (getStringWidth(text) / 2);
        drawStringWithShadow(text, x, y, color);
    }

    public static void drawStringWithShadow(String text, int x, int y, int color) {
        int l = color & 0xff000000;
        int shadow = (color & 0xfcfcfc) >> 2;
        shadow += l;

        drawString(text, x + 3, y + 3, font.getTexture(), shadow);
        drawString(text, x, y, font.getTexture(), color);
    }

    public static int getStringWidth(String text) {
        int fontSize = DEFAULT_FONT_SIZE;

        int width = 0;
        for (int i1 = 0; i1 < text.length(); i1++) {
            int k1 = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~\u2302\307\374\351\342\344\340\345\347\352\353\350\357\356\354\304\305\311\346\306\364\366\362\373\371\377\326\334\370\243\330\327\u0192\341\355\363\372\361\321\252\272\277\256\254\275\274\241\253\273".indexOf(text.charAt(i1));
            if (k1 >= 0) {
                int pos = (k1 + 32);
                width += font.getWidth(pos) * (fontSize / 8f);
            }
        }
        return width;
    }

    public static int getFontHeight() {
        return DEFAULT_FONT_SIZE + 8;
    }

    private static void drawString(String text, int x, int y, Texture texture, int color) {
        float width = texture.getWidth() * 16;
        float height = texture.getWidth() * 16;

        if (text.length() == 0) {
            return;
        }

        int fontSize = DEFAULT_FONT_SIZE;

        float xF = 0;
        for (int i1 = 0; i1 < text.length(); i1++) {
            int k1 = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~\u2302\307\374\351\342\344\340\345\347\352\353\350\357\356\354\304\305\311\346\306\364\366\362\373\371\377\326\334\370\243\330\327\u0192\341\355\363\372\361\321\252\272\277\256\254\275\274\241\253\273".indexOf(text.charAt(i1));
            if (k1 >= 0) {
                int pos = (k1 + 32);

                Vector2f position = getPosition(pos, texture);
                float uvX = position.x / texture.getWidth();
                float uvY = position.y / texture.getHeight();
                float uvWidth = texture.getWidth() / width;
                float uvHeight = texture.getHeight() / height;
                OpenCubes.getInstance().gameRenderer.bindTexture(font.getTexture());
                RenderSystem.disableCull();
                RenderSystem.disableDepthTest();
                RenderSystem.enableBlend();
                Rect.drawRect((int) (x + xF), y, fontSize, fontSize, uvX, uvY, uvWidth, uvHeight, 0xFFFFFF);

                xF += font.getWidth(pos) * (fontSize / 8f);
            }
        }
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
