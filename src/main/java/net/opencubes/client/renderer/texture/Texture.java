package net.opencubes.client.renderer.texture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

public class Texture {
    public static class TextureData {
        private final int id;
        private final String path;
        private int width;
        private int height;

        public TextureData(int id, String path, int width, int height) {
            this.id = id;
            this.path = path;
            this.width = width;
            this.height = height;
        }
    }

    private static final Logger logger = LogManager.getLogger("Texture");

    private final int id;
    private final String path;
    private int width;
    private int height;

    public Texture(String fileName) {
        this(loadTexture(fileName, false));
    }

    public Texture(String fileName, boolean inResources) {
        this(loadTexture(fileName, inResources));
    }

    public Texture(BufferedImage image) {
        this(loadImage(image));
    }

    public Texture(TextureData data) {
        logger.info("Loaded texture: " + data.path + " (" + data.width + "x" + data.height + ")");
        this.id = data.id;
        this.width = data.width;
        this.height = data.height;
        this.path = data.path;
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    private static TextureData loadImage(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) ((pixel) & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        // Create a new OpenGL texture
        int textureId = GL11.glGenTextures();

        // Bind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);


        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Upload the texture data
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        // Generate Mip Map
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        return new TextureData(textureId, "", image.getWidth(), image.getHeight());
    }

    private static TextureData loadTexture(String fileName, boolean inResources) {
        TextureData data = null;
        if (inResources) {
            logger.info("Loading texture from resources: " + fileName);
            try {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(Texture.class.getResourceAsStream(fileName)));
                data = loadImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("Loading texture from path: " + fileName);
            data = loadSTB(fileName);
        }
        return data;
    }

    private static TextureData loadSTB(String fileName) {
        int width;
        int height;
        ByteBuffer buf;
        // Load Texture file
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = STBImage.stbi_load(fileName, w, h, channels, 4);
            if (buf == null) {
                throw new Exception("Image file [" + fileName + "] not loaded: " + STBImage.stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Create a new OpenGL texture
        int textureId = GL11.glGenTextures();
        // Bind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Upload the texture data
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
        // Generate Mip Map
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        STBImage.stbi_image_free(buf);
        return new TextureData(textureId, fileName, width, height);
    }

    public void cleanup() {
        GL11.glDeleteTextures(id);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getPath() {
        return path;
    }
}