package net.opencubes.client.systems;

import net.opencubes.client.Camera;
import net.opencubes.client.OpenCubes;
import net.opencubes.client.vertex.Model;
import net.opencubes.world.physics.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public final class RenderSystem {
    private RenderSystem() {}

    private static final Matrix4f ortho = new Matrix4f();
    private static final Matrix4f projection = new Matrix4f();
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000;

    public static final int TEXTURE_BANK_0 = GL30.GL_TEXTURE0;

    /* Projections */

    public static Matrix4f updateProjection() {
        int width = OpenCubes.getInstance().getWindow().getWidth();
        int height = OpenCubes.getInstance().getWindow().getHeight();
        return updateProjection(width, height);
    }

    private static Matrix4f updateProjection(int width, int height) {
        float aspectRatio = (float) width / height;
        return projection.setPerspective((float) Math.toRadians(70), aspectRatio, Z_NEAR, Z_FAR);
    }

    public static Matrix4f updateOrthographicProjection() {
        int width = OpenCubes.getInstance().getWindow().getWidth();
        int height = OpenCubes.getInstance().getWindow().getHeight();
        return ortho.identity().ortho(-(width / 2.0f), width / 2.0f,
                -(height / 2.0f), (height / 2.0f),
                -1000.0f, 1000.0f);
    }

    public static Matrix4f getProjection() {
        return projection;
    }

    public static Matrix4f getOrthoProjection() {
        return ortho;
    }

    /* View */

    private static final Matrix4f viewMatrix = new Matrix4f();
    private static final Matrix4f modelViewMatrix = new Matrix4f();

    public static Matrix4f getViewMatrix(Camera camera) {
        Vec3 cameraPos = camera.getPosition();
        float yaw = camera.getYaw();
        float pitch = camera.getPitch();

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float) Math.toRadians(yaw), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(pitch), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public static Matrix4f getModelMatrix(Model model) {
        Vec3 rotation = model.getRotation();
        modelViewMatrix.identity().translate(new Vector3f(model.getPosition().x, model.getPosition().y, model.getPosition().z)).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z))
                .scale(model.getScale());
        return modelViewMatrix;
    }

    public static Matrix4f getModelViewMatrix(Model model, Matrix4f viewMatrix) {
        Vec3 rotation = model.getRotation();
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(getModelMatrix(model));
    }

    public static void setViewport(int width, int height) {
        GL11.glViewport(0, 0, width, height);
    }

    /* Targets */

    public static void clearBuffers() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public static void clearColorBuffer() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public static void clearDepthBuffer() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
    }

    public static void clearColor(float r, float g, float b) {
        GL11.glClearColor(r, g, b, 1);
    }

    public static void activateTexture(int bank) {
        GL30.glActiveTexture(bank);
    }

    public static void deactivateTexture() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
    }

    public static void enableDepthTest() {
        enable(GL11.GL_DEPTH_TEST);
    }

    public static void disableDepthTest() {
        enable(GL11.GL_DEPTH_TEST);
    }

    public static void enableCull() {
        enable(GL11.GL_CULL_FACE);
    }

    public static void disableCull() {
        disable(GL11.GL_CULL_FACE);
    }

    public static void enableBlend() {
        enable(GL11.GL_BLEND);
    }

    public static void disableBlend() {
        disable(GL11.GL_BLEND);
    }

    public static void enableMSAA() {
        enable(GL30.GL_MULTISAMPLE);
    }

    public static void disableMSAA() {
        enable(GL30.GL_MULTISAMPLE);
    }

    private static void enable(int target) {
        GL11.glEnable(target);
    }

    private static void disable(int target) {
        GL11.glDisable(target);
    }
}