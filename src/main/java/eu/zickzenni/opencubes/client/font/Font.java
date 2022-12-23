package eu.zickzenni.opencubes.client.font;

import eu.zickzenni.opencubes.client.texture.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Font {
    private final Texture texture;
    private int[] charWidth;

    public Font(Texture texture) {
        if (texture.getWidth() != texture.getHeight()) {
            throw new IllegalArgumentException("Font texture size is not 1:1!");
        }
        if (texture.getWidth() < 16 || texture.getHeight() < 16) {
            throw new IllegalArgumentException("Font texture size cannot be smaller than 16!");
        }
        if (texture.getWidth() % 16 != 0) {
            throw new IllegalArgumentException("Font texture size is not a power of 2!");
        }
        this.texture = texture;
        this.charWidth = new int[256];

        BufferedImage bufferedimage;
        try
        {
            bufferedimage = ImageIO.read((Font.class).getResourceAsStream(texture.getPath()));
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
