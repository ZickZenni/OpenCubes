package eu.zickzenni.opencubes.client.render;

import eu.zickzenni.opencubes.client.OpenCubes;
import eu.zickzenni.opencubes.client.mesh.CubeMesh;
import eu.zickzenni.opencubes.client.mesh.Mesh;
import eu.zickzenni.opencubes.client.mesh.MeshBuilder;
import eu.zickzenni.opencubes.client.shader.Shader;
import eu.zickzenni.opencubes.client.shader.ShaderManager;
import eu.zickzenni.opencubes.world.Dimension;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class RenderSystem {
    public static void renderModel(Model model) {
        renderModel(ShaderManager.getShader("default"), model);
    }

    public static void renderModel(Shader shader, Model model) {
        renderModel(shader, model, true);
    }

    public static void renderModel(Shader shader, Model model, boolean depth) {
        shader.bind();

        if (depth) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } else {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);

        // Update projection Matrix
        Matrix4f projectionMatrix = OpenCubes.getInstance().getWindow().updateProjection();
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = Transform.getViewMatrix();

        //shader.setUniform("texture_sampler", 0);
        Matrix4f modelViewMatrix = Transform.getModelViewMatrix(model, viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);
        model.getMesh().render();

        shader.unbind();
    }

    public static void renderSelectionBox(Dimension dimension, int x, int y, int z) {
        MeshBuilder builder = new MeshBuilder();
        if (!dimension.doesBlockExist(x, y + 1, z))
            builder.addFace(CubeMesh.getTopFace(0));
        if (!dimension.doesBlockExist(x, y - 1, z))
            builder.addFace(CubeMesh.getBottomFace(0));
        if (!dimension.doesBlockExist(x - 1, y, z))
            builder.addFace(CubeMesh.getLeftFace(0));
        if (!dimension.doesBlockExist(x + 1, y, z))
            builder.addFace(CubeMesh.getRightFace(0));
        if (!dimension.doesBlockExist(x, y, z + 1))
            builder.addFace(CubeMesh.getFrontFace(0));
        if (!dimension.doesBlockExist(x, y, z - 1))
            builder.addFace(CubeMesh.getBackFace(0));

        Mesh mesh = builder.build();
        Model model = new Model(mesh, 1, new Vector3f(x + 0.5f, y, z + 0.5f));
        GL11.glDisable(GL11.GL_CULL_FACE);
        renderModel(ShaderManager.getShader("selection"), model, false);
        model.getMesh().cleanup();
    }
}