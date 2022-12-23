package eu.zickzenni.opencubes.client.world;

import eu.zickzenni.opencubes.client.OpenCubes;

public class WorldRenderer {
    public static void renderWorld() {
        if (OpenCubes.getInstance().getWorld() == null || OpenCubes.getInstance().getCamera() == null) {
            OpenCubes.getInstance().getWindow().setClearColor(0,0,0);
            return;
        }

        OpenCubes.getInstance().getWindow().setClearColor(0.25f, 0.75f, 1.0f);

        OpenCubes.getInstance().getPlayer().getDimension().render();
    }
}
