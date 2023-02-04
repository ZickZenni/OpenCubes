package net.opencubes.client.gui;

import net.opencubes.client.OpenCubes;
import net.opencubes.client.gui.components.Rect;
import net.opencubes.client.gui.components.Widget;
import net.opencubes.client.renderer.FontRenderer;
import net.opencubes.client.renderer.ItemRenderer;
import net.opencubes.client.renderer.texture.Texture;
import net.opencubes.client.shader.ShaderManager;
import net.opencubes.client.systems.RenderSystem;
import net.opencubes.client.vertex.Mesh;
import net.opencubes.client.vertex.Model;
import net.opencubes.entity.player.LocalPlayer;
import net.opencubes.inventory.ItemStack;
import net.opencubes.world.physics.Vec3;

public class IngameGui implements Widget {
    public static final Texture GUI = new Texture("/assets/textures/gui/widgets.png", true);
    private static final int CROSSHAIR_SIZE = 40;
    private static final int HOTBAR_WIDTH = 546;
    private static final int HOTBAR_HEIGHT = 66;

    @Override
    public void render(int mouseX, int mouseY, float deltaTime) {
        LocalPlayer player = OpenCubes.getInstance().player;
        if (player == null) {
            return;
        }

        renderViewModel();
        renderHud();
    }

    private void renderViewModel() {
        LocalPlayer player = OpenCubes.getInstance().player;
        int width = OpenCubes.getInstance().getWindow().getWidth();
        int height = OpenCubes.getInstance().getWindow().getHeight();

        if (player.getInventory().getItemStack(27 + player.getInventory().getHotbarSlot()) != null) {
            final float size = 500;
            Mesh mesh = player.getInventory().getItemStack(27 + player.getInventory().getHotbarSlot()).getItem().get3DModel(size);
            if (mesh != null) {
                RenderSystem.disableBlend();
                RenderSystem.enableCull();
                RenderSystem.setCullFront();
                RenderSystem.disableDepthTest();

                OpenCubes.getInstance().gameRenderer.bindTexture(OpenCubes.getInstance().atlas.getTexture());
                OpenCubes.getInstance().gameRenderer.renderModelOrthographic(new Model(mesh, 1, new Vec3(width - 210, height - 100 - 4, 100), new Vec3(28, -40, 0f)), ShaderManager.getShader("default"));

                RenderSystem.setCullBack();

                mesh.cleanup();
            }
        }
    }

    private void renderHud() {
        LocalPlayer player = OpenCubes.getInstance().player;
        int width = OpenCubes.getInstance().getWindow().getWidth();
        int height = OpenCubes.getInstance().getWindow().getHeight();

        OpenCubes.getInstance().gameRenderer.bindTexture(GUI);
        Rect.drawRect(width / 2 - (CROSSHAIR_SIZE / 2), height / 2 - (CROSSHAIR_SIZE / 2), CROSSHAIR_SIZE, CROSSHAIR_SIZE, (256 - 16) / 256f, 0, 15 / 256f, 15 / 256f, 0xFFFFFF);

        OpenCubes.getInstance().gameRenderer.bindTexture(GUI);
        Rect.drawRect(width / 2 - (HOTBAR_WIDTH / 2), height - HOTBAR_HEIGHT, HOTBAR_WIDTH, HOTBAR_HEIGHT, 0, 0, 182 / 256f, 22 / 256f, 0xFFFFFF);

        for (int i = 0; i < 9; i++) {
            float slotX = width / 2f - (HOTBAR_WIDTH / 2f) + 5 + (60 * i);
            ItemStack itemStack = player.getInventory().getItemStack(27 + i);
            ItemRenderer.drawItemStack(itemStack, slotX, height);
        }

        int x = width / 2 - (HOTBAR_WIDTH / 2) + (player.getInventory().getHotbarSlot() * (60));
        OpenCubes.getInstance().gameRenderer.bindTexture(GUI);
        Rect.drawRect(x, height - HOTBAR_HEIGHT, HOTBAR_HEIGHT, HOTBAR_HEIGHT, 0, 22 / 256f, 24 / 256f, 24 / 256f, 0xFFFFFFFF);

        FontRenderer.drawString("FPS: " + OpenCubes.getInstance().getFps(), 10, 10, 0xFFFFFF);
        //FontRenderer.drawString("Position: " + OpenCubes.getInstance().player.getPosition(), 10, 36, 0xFFFFFF);
        //FontRenderer.drawString("Vertices: " + OpenCubes.getInstance().gameRenderer.getVertexCount(), 10, 62, 0xFFFFFF);
    }
}