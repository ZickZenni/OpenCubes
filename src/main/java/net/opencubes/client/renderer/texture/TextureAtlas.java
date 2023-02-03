package net.opencubes.client.renderer.texture;

import net.opencubes.client.block.model.BlockModel;
import net.opencubes.client.block.model.BlockModelManager;
import net.opencubes.client.block.model.BlockModelTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TextureAtlas {
    private final Texture texture;
    private HashMap<String, AtlasPosition> positions = new HashMap<>();

    public TextureAtlas() {
        HashMap<String, String> textures = new HashMap<>();
        for (Map.Entry<String, BlockModel> entry : BlockModelManager.getModels().entrySet()) {
            BlockModel model = entry.getValue();
            for (Map.Entry<String, BlockModelTexture> entry2 : model.getTextures().entrySet()) {
                BlockModelTexture texture = entry2.getValue();
                if (!textures.containsKey(texture.texture())) {
                    textures.put(texture.texture(), "/assets/textures/" + texture.texture() + ".png");
                }
            }
        }

        int pixels = 0;

        HashMap<String, BufferedImage> bufferedImages = new HashMap<>();
        for (Map.Entry<String, String> entry : textures.entrySet()) {
            String name = entry.getKey();
            String filePath = entry.getValue();
            try {
                InputStream stream = TextureAtlas.class.getResourceAsStream(filePath);
                if (stream == null) {
                    continue;
                }
                BufferedImage image = ImageIO.read(stream);
                pixels += image.getWidth() * image.getHeight();
                bufferedImages.put(name, image);
            } catch (IOException ignored) {}
        }

        int atlasSize = 512;
        if (pixels > (atlasSize * atlasSize)) {
            atlasSize = (int) Math.pow(2, 32 - Integer.numberOfLeadingZeros((int) (Math.round(Math.sqrt(pixels)) - 1)));
        }

        BufferedImage atlas = new BufferedImage(atlasSize, atlasSize, BufferedImage.TYPE_INT_ARGB);
        int x = 0;
        int y = 0;

        int highestY = 0;

        for (Map.Entry<String, BufferedImage> entry : bufferedImages.entrySet()) {
            String name = entry.getKey();
            BufferedImage bufferedImage = entry.getValue();
            if (bufferedImage.getHeight() > highestY) {
                highestY = bufferedImage.getHeight();
            }
            if (x + bufferedImage.getWidth() <= atlasSize) {
                for (int i = 0; i < bufferedImage.getWidth(); i++) {
                    for (int j = 0; j < bufferedImage.getHeight(); j++) {
                        atlas.setRGB(x + i, y + j, bufferedImage.getRGB(i, j));
                    }
                }

                positions.put(name, new AtlasPosition(x, y, bufferedImage.getWidth(), bufferedImage.getHeight()));

                x += bufferedImage.getWidth();
                if (x >= atlasSize) {
                    x = 0;
                    y += highestY;
                    highestY = 0;
                }
            }
        }

        atlas.setRGB(atlasSize - 2, atlasSize - 2, 0xFFFB3EF9);
        atlas.setRGB(atlasSize - 1, atlasSize - 1, 0xFFFB3EF9);
        atlas.setRGB(atlasSize - 2, atlasSize - 1, 0xFF000000);
        atlas.setRGB(atlasSize - 1, atlasSize - 2, 0xFF000000);
        atlas.setRGB(atlasSize - 3, atlasSize - 1, 0xFFFFFFFF);
        texture = new Texture(atlas);
    }

    public Texture getTexture() {
        return texture;
    }

    public AtlasPosition getPosition(String name) {
        return positions.getOrDefault(name, null);
    }

    public Map<String, AtlasPosition> getPositions() {
        return Collections.unmodifiableMap(positions);
    }
}