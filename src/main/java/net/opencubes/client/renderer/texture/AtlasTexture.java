package net.opencubes.client.renderer.texture;

public class AtlasTexture {
    private final String name;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final boolean animated;

    private int currentTicks = 0;
    private int currentFrame = 0;
    private final int frameAmount;
    private final int frameTime;
    private final int[] frames;

    public AtlasTexture(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.animated = false;
        this.frameAmount = 0;
        this.frameTime = -1;
        this.frames = null;
    }

    public AtlasTexture(String name, int x, int y, int width, int height, int frameAmount, int frameTime, int[] frames) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.animated = true;
        this.frameAmount = frameAmount;
        this.frameTime = frameTime;
        this.frames = frames;
    }

    public void tick() {
        if (animated) {
            if (currentTicks < frameTime) {
                currentTicks++;
                if (currentTicks >= frameTime) {
                    currentTicks = 0;
                    currentFrame++;
                    if (currentFrame >= frames.length) {
                        currentFrame = 0;
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isAnimated() {
        return animated;
    }

    public int getFrameAmount() {
        return frameAmount;
    }

    public int getFrameTime() {
        return frameTime;
    }

    public int[] getFrames() {
        return frames;
    }

    public float getCurrentFrame() {
        return currentFrame;
    }
}