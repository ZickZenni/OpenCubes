package net.opencubes.client.gui.components;

import net.opencubes.client.OpenCubes;
import net.opencubes.client.shader.ShaderManager;
import net.opencubes.client.systems.RenderSystem;
import net.opencubes.client.vertex.Mesh;
import net.opencubes.client.vertex.Model;
import net.opencubes.inventory.ItemStack;
import net.opencubes.world.physics.Vec3;

public class ItemRenderer {
    public static void drawItemStack(ItemStack itemStack, float x, float y) {
        if (itemStack == null)
            return;

        final float size = 28;
        Mesh mesh = itemStack.getItem().get3DModel(size);
        if (mesh != null) {
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.setCullFront();
            RenderSystem.disableDepthTest();

            OpenCubes.getInstance().gameRenderer.bindTexture(OpenCubes.getInstance().atlas.getTexture());
            OpenCubes.getInstance().gameRenderer.renderModelOrthographic(new Model(mesh, 1, new Vec3(x + size, y - size - 4, 100), new Vec3(32f, 45f, 0f)), ShaderManager.getShader("default"));

            RenderSystem.setCullBack();
        }
    }
}