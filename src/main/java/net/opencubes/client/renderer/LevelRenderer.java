package net.opencubes.client.renderer;

import com.beust.jcommander.internal.Nullable;
import net.opencubes.client.Camera;
import net.opencubes.client.OpenCubes;
import net.opencubes.client.shader.ShaderManager;
import net.opencubes.client.systems.RenderSystem;
import net.opencubes.client.vertex.Model;
import net.opencubes.client.vertex.util.CubeMesh;
import net.opencubes.entity.player.LocalPlayer;
import net.opencubes.world.level.ChunkPos;
import net.opencubes.world.level.Level;
import net.opencubes.world.level.chunk.LevelChunk;
import net.opencubes.world.physics.Vec3;

import java.util.Map;

public class LevelRenderer {
    public static int renderDistance = 8;

    private final OpenCubes openCubes;
    @Nullable
    private Level level;

    private final Model skyModel;

    public LevelRenderer(OpenCubes openCubes) {
        this.openCubes = openCubes;
        this.skyModel = new Model(CubeMesh.createCubeMesh(null, 64 / 255f, 132 / 255f, 255 / 255f, 1, 300, 1, 300, false), 1, new Vec3(0, 0, 0));
    }

    public void renderLevel(Camera camera) {
        if (level == null)
            return;
        renderSky(camera);

        level.tickFrame();

        try {
            LocalPlayer player = OpenCubes.getInstance().player;
            int plrChunkX = (int) player.getPosition().x / 16;
            int plrChunkZ = (int) player.getPosition().z / 16;
            for (int x = plrChunkX - renderDistance; x < plrChunkX + renderDistance; x++) {
                for (int z = plrChunkZ - renderDistance; z < plrChunkZ + renderDistance; z++) {
                    ChunkPos position = new ChunkPos(x, z);
                    LevelChunk chunk = level.getChunk(position);
                    if (chunk != null) {
                        RenderSystem.disableBlend();
                        RenderSystem.enableDepthTest();
                        RenderSystem.enableCull();
                        openCubes.gameRenderer.bindTexture(openCubes.atlas.getTexture());
                        for (Map.Entry<String, Model> entry : chunk.getMesh().getOpaqueModels().entrySet()) {
                            openCubes.gameRenderer.renderModel(camera, entry.getValue(), ShaderManager.getShader(entry.getKey()));
                        }
                    }
                }
            }
        } catch (Exception ignore) {
        }

        if (openCubes.player.selectedBlock != null) {
            int x = openCubes.player.selectedBlock.getAbsoluteX();
            int y = openCubes.player.selectedBlock.getY();
            int z = openCubes.player.selectedBlock.getAbsoluteZ();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            openCubes.gameRenderer.renderSelectionBox(camera, level, x, y, z);
        }
    }

    private void renderSky(Camera camera) {
        RenderSystem.clearColor(84 / 255f, 146 / 255f, 255 / 255f);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        skyModel.setPosition(camera.getPosition().x, camera.getPosition().y + 100, camera.getPosition().z);
        openCubes.gameRenderer.bindTexture(openCubes.atlas.getTexture());
        openCubes.gameRenderer.renderModel(camera, skyModel, ShaderManager.getShader("default"));

        // Void
        skyModel.setPosition(camera.getPosition().x, camera.getPosition().y >= 0 ? -100 : camera.getPosition().y - 100, camera.getPosition().z);
        openCubes.gameRenderer.bindTexture(openCubes.atlas.getTexture());
        openCubes.gameRenderer.renderModel(camera, skyModel, ShaderManager.getShader("default"));
    }

    public void setLevel(@Nullable Level level) {
        this.level = level;
    }

    public void regenerateChunks() {
        if (level == null) {
            return;
        }
        LocalPlayer player = OpenCubes.getInstance().player;
        int plrChunkX = (int) player.getPosition().x / 16;
        int plrChunkZ = (int) player.getPosition().z / 16;
        for (int x = plrChunkX - renderDistance; x < plrChunkX + renderDistance; x++) {
            for (int z = plrChunkZ - renderDistance; z < plrChunkZ + renderDistance; z++) {
                ChunkPos position = new ChunkPos(x, z);
                LevelChunk chunk = level.getChunk(position);
                if (chunk != null) {
                    chunk.setGenerateMesh();
                }
            }
        }
    }
}
