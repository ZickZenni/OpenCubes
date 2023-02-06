package net.opencubes.client.renderer;

import net.opencubes.client.Camera;
import net.opencubes.client.OpenCubes;
import net.opencubes.client.renderer.texture.Texture;
import net.opencubes.client.shader.Shader;
import net.opencubes.client.shader.ShaderManager;
import net.opencubes.client.systems.RenderSystem;
import net.opencubes.client.vertex.Mesh;
import net.opencubes.client.vertex.Model;
import net.opencubes.client.vertex.util.CubeMesh;
import net.opencubes.client.vertex.util.MeshBuilder;
import net.opencubes.world.level.Level;
import net.opencubes.world.physics.Vec2;
import net.opencubes.world.physics.Vec3;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameRenderer {
    private final Camera mainCamera = new Camera(null, true);
    private final LevelRenderer levelRenderer;
    private Texture boundTexture;

    private int vertexCount = 0;

    public GameRenderer(OpenCubes openCubes) {
        this.levelRenderer = new LevelRenderer(openCubes);
    }

    /**
     * Render a model to the screen
     * @param camera
     * @param model
     * @param shader
     */
    public void renderModel(Camera camera, Model model, Shader shader) {
        if (model == null || camera == null || shader == null) {
            return;
        }

        if (!shader.isBound()) {
            shader.bind(model);
        }
        if (boundTexture != null) {
            RenderSystem.activateTexture(RenderSystem.TEXTURE_BANK_0);
            boundTexture.bind();
        }

        vertexCount += model.getMesh().getVertexCount();

        Matrix4f projectionMatrix = RenderSystem.updateProjection();
        shader.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = RenderSystem.getViewMatrix(camera);

        Matrix4f modelViewMatrix = RenderSystem.getModelViewMatrix(model, viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);
        model.getMesh().render();

        shader.unbind();

        RenderSystem.deactivateTexture();
        boundTexture = null;
    }

    /**
     * Render a model to the screen in orthographic projection
     * @param model
     * @param shader
     */
    public void renderModelOrthographic(Model model, Shader shader) {
        if (model == null || shader == null) {
            return;
        }

        float width = OpenCubes.getInstance().getWindow().getWidth();
        float height = OpenCubes.getInstance().getWindow().getHeight();

        if (!shader.isBound()) {
            shader.bind(model);
        }
        if (boundTexture != null) {
            RenderSystem.activateTexture(RenderSystem.TEXTURE_BANK_0);
            boundTexture.bind();
        }

        vertexCount += model.getMesh().getVertexCount();

        // Update projection Matrix
        Matrix4f projectionMatrix = RenderSystem.updateOrthographicProjection();
        shader.setUniform("projectionMatrix", projectionMatrix);

        Vec3 rotation = model.getRotation();
        Matrix4f modelView = new Matrix4f().translate(new Vector3f(-(width / 2f) + model.getPosition().x, (height / 2f) - model.getPosition().y, 1)).
                rotateX((float) java.lang.Math.toRadians(-rotation.x)).
                rotateY((float) java.lang.Math.toRadians(-rotation.y)).
                rotateZ((float) java.lang.Math.toRadians(-rotation.z))
                .scale(model.getScale());

        shader.setUniform("modelViewMatrix", modelView);
        model.getMesh().render();

        shader.unbind();

        RenderSystem.deactivateTexture();
        boundTexture = null;
    }

    public void renderLevel() {
        this.levelRenderer.renderLevel(this.mainCamera);
    }

    public void renderSelectionBox(Camera camera, Level level, int x, int y, int z) {
        MeshBuilder builder = new MeshBuilder();
        if (level.getBlock(x, y + 1, z) == null)
            builder.addFace(CubeMesh.getTopFace(""));
        if (level.getBlock(x, y - 1, z) == null)
            builder.addFace(CubeMesh.getBottomFace(""));
        if (level.getBlock(x - 1, y, z) == null)
            builder.addFace(CubeMesh.getLeftFace(""));
        if (level.getBlock(x + 1, y, z) == null)
            builder.addFace(CubeMesh.getRightFace(""));
        if (level.getBlock(x, y, z + 1) == null)
            builder.addFace(CubeMesh.getFrontFace(""));
        if (level.getBlock(x, y, z - 1) == null)
            builder.addFace(CubeMesh.getBackFace(""));

        Mesh mesh = builder.build();
        Model model = new Model(mesh, 1.02f, new Vec3(x + 0.5f, y, z + 0.5f));
        renderModel(camera, model, ShaderManager.getShader("selection"));
        model.getMesh().cleanup();
    }

    public void updateCamera(float deltaTime) {
        Vec2 rotVec = OpenCubes.getInstance().getWindow().getMouseInput().getDisplVec();
        moveCameraRotation(rotVec.x * 20 / 100f, rotVec.y * 20 / 100f);
        mainCamera.update(deltaTime);
    }

    private void moveCameraRotation(float offsetX, float offsetY) {
        if (OpenCubes.getInstance().getWindow().isFocused()) {
            mainCamera.setYaw(Math.clamp(-89, 89, mainCamera.getYaw() + offsetX));
            mainCamera.setPitch(mainCamera.getPitch() + offsetY);
        }
    }

    public void bindTexture(Texture texture) {
        this.boundTexture = texture;
    }

    public LevelRenderer getLevelRenderer() {
        return levelRenderer;
    }

    public Camera getMainCamera() {
        return mainCamera;
    }

    public Texture getBoundTexture() {
        return boundTexture;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void resetVertexCount() {
        vertexCount = 0;
    }
}