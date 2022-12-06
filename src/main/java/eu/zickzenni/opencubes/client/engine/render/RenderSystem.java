package eu.zickzenni.opencubes.client.engine.render;

import eu.zickzenni.opencubes.OpenCubes;
import eu.zickzenni.opencubes.client.engine.shader.ShaderManager;
import eu.zickzenni.opencubes.client.engine.shader.ShaderProgram;
import eu.zickzenni.opencubes.world.ChunkMeshSystem;
import eu.zickzenni.opencubes.world.World;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.glClearColor;

public class RenderSystem {
    public static void renderModel(Model model) {
        renderModel(ShaderManager.getShader("default"), model);
    }

    public static void renderModel(ShaderProgram shader, Model model) {
        shader.bind();

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
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            return;
        }

        glClearColor(0.52f, 0.8f, 0.92f, 0.0f);

        ChunkMeshSystem.update();
        World.getCamera().getDimension().render();

        /*
            Model model = new Model(new MeshBuilder().addFace(CubeMesh.getFrontFace(2)).build(), 1, new Vector3f(0, 100, 0));
            renderModel(model);
            model.getMesh().cleanup();
        */
    }
}
