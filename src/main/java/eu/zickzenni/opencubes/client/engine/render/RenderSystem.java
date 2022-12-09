package eu.zickzenni.opencubes.client.engine.render;

import eu.zickzenni.opencubes.OpenCubes;
import eu.zickzenni.opencubes.client.engine.shader.ShaderManager;
import eu.zickzenni.opencubes.client.engine.shader.ShaderProgram;
import eu.zickzenni.opencubes.world.ChunkMeshSystem;
import eu.zickzenni.opencubes.world.World;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class RenderSystem {
    public static void renderModel(Model model) {
        renderModel(ShaderManager.getShader("default"), model);
    }

    public static void renderModel(ShaderProgram shader, Model model) {
        shader.bind();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);

        // Update projection Matrix
        Matrix4f projectionMatrix = OpenCubes.getInstance().getWindow().updateProjection();
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = Transform.getViewMatrix();

        shader.setUniform("texture_sampler", 0);
        Matrix4f modelViewMatrix = Transform.getModelViewMatrix(model, viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);
        model.getMesh().render();

        shader.unbind();
    }

    public static void renderWorld() {
        if (OpenCubes.getInstance().getWorld() == null || World.getCamera() == null) {
            OpenCubes.getInstance().getWindow().setClearColor(0,0,0);
            return;
        }

        OpenCubes.getInstance().getWindow().setClearColor(53 / 255f,81 / 255f,92 / 255f);

        ChunkMeshSystem.update();
        World.getCamera().getDimension().render();

        FontRenderer.renderString("Position: "
                + String.format(java.util.Locale.US,"%.2f", World.getCamera().getPosition().x) + ","
                + String.format(java.util.Locale.US,"%.2f", World.getCamera().getPosition().y) + ","
                + String.format(java.util.Locale.US,"%.2f", World.getCamera().getPosition().z)
                , 3, 3, 1, 0xFFFFFF);
    }
}