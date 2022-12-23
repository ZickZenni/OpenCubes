package eu.zickzenni.opencubes.client.gui.screen;

import eu.zickzenni.opencubes.client.OpenCubes;

public class Screen {
    private int width;
    private int height;

    public final void init() {
        this.width = OpenCubes.getInstance().getWindow().getWidth();
        this.height = OpenCubes.getInstance().getWindow().getHeight();
        onInit();
    }

    public final void render() {
        onRender();
    }

    public void onInit() {

    }

    public void onRender() {

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
