package eu.zickzenni.opencubes.client.engine.render;

import eu.zickzenni.opencubes.OpenCubes;
import eu.zickzenni.opencubes.client.engine.shader.ShaderManager;
import eu.zickzenni.opencubes.client.engine.shader.ShaderProgram;
import eu.zickzenni.opencubes.world.ChunkMeshSystem;
import eu.zickzenni.opencubes.world.World;
import org.joml.Matrix4f;

public class RenderSystem {
    public static void render3dModel(Model model) {
        if (model == null)
            return;

        ShaderProgram shaderProgram = ShaderManager.getShader("default");
        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = OpenCubes.getInstance().getWindow().updateProjection();
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = Transform.getViewMatrix();

        shaderProgram.setUniform("texture_sampler", 0);
        Matrix4f modelViewMatrix = Transform.getModelViewMatrix(model, viewMatrix);
        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        model.getMesh().render();

        shaderProgram.unbind();
    }

    public static void renderWorld() {
        if (OpenCubes.getInstance().getWorld() == null)
            return;
        if (World.getCamera() == null)
            return;

        ChunkMeshSystem.update();
        World.getCamera().getDimension().render();
    }
}
