package net.opencubes.client.gui.screens;

import net.opencubes.client.OpenCubes;
import net.opencubes.client.gui.components.Rect;
import net.opencubes.client.renderer.texture.Texture;

import java.awt.*;

public class TitleScreen extends Screen {
    private Texture logo;

    private float startTime = 5;

    private float fadeInTime = 2;
    private float logoTime = 5;
    private float fadeOutTime = 2;

    public TitleScreen() {
        this.logo = new Texture("/assets/textures/gui/logo.png", true);
    }

    @Override
    public void render(int mouseX, int mouseY, float deltaTime) {
        Rect.drawRect(0, 0, width, height,0x000000);

        if (startTime > 0) {
            startTime -= deltaTime;
        } else {
            if (fadeInTime > 0) {
                fadeInTime -= deltaTime;
            } else {
                if (logoTime > 0) {
                    logoTime -= deltaTime;
                }  else {
                    if (fadeOutTime > 0) {
                        fadeOutTime -= deltaTime;
                    }
                }
            }

            float alpha = 1;
            if (fadeInTime > 0) {
                alpha = (1 / 2f) * fadeInTime;
            }
            if (fadeOutTime > 0) {
                alpha = 1 - ((1 / 2f) * fadeOutTime);
            }
            OpenCubes.getInstance().gameRenderer.bindTexture(logo);
            Rect.drawRect(width / 2 - (517 / 2), height / 2 - (94 / 2), 517, 94, new Color(1,1,1, alpha).getRGB());
        }
    }
}