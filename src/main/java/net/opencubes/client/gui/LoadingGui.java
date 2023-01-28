package net.opencubes.client.gui;

import net.opencubes.client.OpenCubes;
import net.opencubes.client.gui.components.Rect;
import net.opencubes.client.renderer.texture.Texture;
import org.joml.Math;

public class LoadingGui {
    private int progress;
    private Texture texture;
    private Texture loading1;

    private float backgroundTime = 15f;

    public LoadingGui() {
        texture = new Texture("/assets/textures/gui/loading.png", true);
        loading1 = new Texture("/assets/textures/gui/loading/loading1.png", true);
    }

    public void render(int mouseX, int mouseY, int width, int height, float deltaTime) {
        backgroundTime -= deltaTime;
        if (backgroundTime <= 0) {
            backgroundTime = 15;
        }

        int barHeight = 160;

        int bgX = (int) (64 - (64 * (1 / 15f * backgroundTime)));
        OpenCubes.getInstance().gameRenderer.bindTexture(loading1);
        Rect.drawRect(-64 + bgX, -64, width + 64, height + 64,0xFFFFFF);

        Rect.drawRect(0, 0, width, barHeight, 0);
        Rect.drawRect(0, height - barHeight, width, barHeight, 0);

        float loadingSize = 1f / texture.getWidth();
        float loadingUVWidth = loadingSize * 182;
        float loadingUVHeight = loadingSize * 5;

        OpenCubes.getInstance().gameRenderer.bindTexture(texture);
        Rect.drawRect(
                width / 2 - 182 * 2,
                height - (barHeight / 2) - 10,
                (182) * 4,
                5 * 4,
                0,
                0,
                loadingUVWidth,
                loadingUVHeight,
                0xFFFFFF);

        float barProgress = (1 / 100f) * progress;

        OpenCubes.getInstance().gameRenderer.bindTexture(texture);
        Rect.drawRect(
                width / 2 - 182 * 2,
                height - (barHeight / 2) - 10,
                (int) (((182) * 4) * barProgress),
                5 * 4,
                0,
                loadingUVHeight,
                loadingUVWidth * barProgress,
                loadingUVHeight,
                0xFFFFFF);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = Math.clamp(0, 100, progress);
    }

    public void addProgress(int progress) {
        setProgress(this.progress + progress);
    }
}
