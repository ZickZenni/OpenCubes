package net.opencubes.client.gui.screens;

import net.opencubes.client.OpenCubes;
import net.opencubes.client.gui.components.Widget;

public abstract class Screen implements Widget {
    protected OpenCubes openCubes;
    protected int width;
    protected int height;

    public final void init(OpenCubes openCubes, int width, int height) {
        this.openCubes = openCubes;
        this.width = width;
        this.height = height;
        this.init();
    }

    protected void init() {
    }

    public void tick() {
    }

    public boolean isPauseScreen() {
        return true;
    }
}