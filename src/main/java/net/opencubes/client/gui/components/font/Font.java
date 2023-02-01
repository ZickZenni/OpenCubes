package net.opencubes.client.gui.components.font;

import net.opencubes.client.renderer.texture.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Font {
    private final Texture texture;
    private int[] charWidth;

    public Font(String path) {
        this.texture = new Texture(path, true);
        this.charWidth = new int[256];

        BufferedImage bufferedimage;
        try
        {
            bufferedimage = ImageIO.read((Font.class).getResourceAsStream(path));
        }
        catch(IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int[] ai = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, ai, 0, i);
        for(int k = 0; k < 256; k++)
        {
            int l = k % 16;
            int k1 = k / 16;
            int j2 = 7;
            do
            {
                if(j2 < 0)
                {
                    break;
                }
                int i3 = l * 8 + j2;
                boolean flag = true;
                for(int l3 = 0; l3 < 8; l3++)
                {
                    int i4 = (k1 * 8 + l3) * i;
                    int k4 = ai[i3 + i4] & 0xff;
                    if (k4 > 0) {
                        flag = false;
                        break;
                    }
                }

                if(!flag)
                {
                    break;
                }
                j2--;
            } while(true);
            if(k == 32)
            {
                j2 = 2;
            }
            charWidth[k] = j2 + 2;
        }
    }

    public void cleanup() {
        texture.cleanup();
    }

    public Texture getTexture() {
        return texture;
    }

    public int getWidth(int k) {
        return charWidth[k];
    }
}
