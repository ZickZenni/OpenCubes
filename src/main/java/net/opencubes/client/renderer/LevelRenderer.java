package net.opencubes.client.renderer;

import com.beust.jcommander.internal.Nullable;
import net.opencubes.block.Block;
import net.opencubes.block.BlockRegistry;
import net.opencubes.client.Camera;
import net.opencubes.client.OpenCubes;
import net.opencubes.client.level.chunk.ChunkMesh;
import net.opencubes.client.renderer.texture.AtlasTexture;
import net.opencubes.client.shader.Shader;
import net.opencubes.client.shader.ShaderManager;
import net.opencubes.client.systems.RenderSystem;
import net.opencubes.client.vertex.Model;
import net.opencubes.client.vertex.util.CubeMesh;
import net.opencubes.entity.player.LocalPlayer;
import net.opencubes.world.level.ChunkPos;
import net.opencubes.world.level.Level;
import net.opencubes.world.level.chunk.LevelChunk;
import net.opencubes.world.physics.Vec2;
import net.opencubes.world.physics.Vec3;

import java.util.ArrayList;
import java.util.Map;

public class LevelRenderer {
    public static int renderDistance = 8;

    public static final String SHADER = "chunk";

    private final OpenCubes openCubes;
    @org.jetbrains.annotations.Nullable
    private Level level;

    private final Model skyModel;
    //private final Model sunModel;

    public boolean showChunkBorders = false;

    public LevelRenderer(OpenCubes openCubes) {
        this.openCubes = openCubes;
        this.skyModel = new Model(CubeMesh.createCubeMesh(null, 64 / 255f, 132 / 255f, 255 / 255f, 1, 300, 1, 300, false), 1, new Vec3(0, 0, 0));
        //this.sunModel = new Model(CubeMesh.createCubeMesh("null", 1, 1, 1, 1, 300, 1, 300, false), 1, new Vec3(0, 0, 0));
    }

    public void renderLevel(Camera camera) {
        if (level == null)
            return;
        renderSky(camera);

        level.tickFrame();

        try {
            ArrayList<LevelChunk> chunks = new ArrayList<>();

            LocalPlayer player = OpenCubes.getInstance().player;
            int plrChunkX = (int) player.getPosition().x / 16;
            int plrChunkZ = (int) player.getPosition().z / 16;
            for (int x = plrChunkX - renderDistance; x < plrChunkX + renderDistance; x++) {
                for (int z = plrChunkZ - renderDistance; z < plrChunkZ + renderDistance; z++) {
                    ChunkPos position = new ChunkPos(x, z);
                    LevelChunk chunk = level.getChunk(position);
                    if (chunk != null) {
                        chunks.add(chunk);
                    }
                }
            }

            chunks.sort((o1, o2) -> {
                double o1Distance = o1.getDistance(openCubes.gameRenderer.getMainCamera().getPosition());
                double o2Distance = o2.getDistance(openCubes.gameRenderer.getMainCamera().getPosition());
                if (o1Distance == o2Distance) {
                    return 0;
                }

                return o1Distance < o2Distance ? -1 : 1;
            });

            for (LevelChunk chunk : chunks) {
                RenderSystem.disableBlend();
                RenderSystem.enableDepthTest();
                RenderSystem.enableCull();
                renderChunk(chunk, false);
            }

            if (openCubes.player.selectedBlock != null) {
                Block block = BlockRegistry.getBlock(openCubes.player.selectedBlock.getBlockName());
                if (block != null && !block.isFluid()) {
                    int x = openCubes.player.selectedBlock.getAbsoluteX();
                    int y = openCubes.player.selectedBlock.getY();
                    int z = openCubes.player.selectedBlock.getAbsoluteZ();
                    RenderSystem.enableBlend();
                    RenderSystem.enableDepthTest();
                    RenderSystem.enableCull();
                    openCubes.gameRenderer.renderSelectionBox(camera, level, x, y, z);
                }
            }

            for (LevelChunk chunk : chunks) {
                RenderSystem.enableBlend();
                RenderSystem.enableDepthTest();
                RenderSystem.enableCull();
                renderChunk(chunk, true);
            }

            if (showChunkBorders) {
                RenderSystem.enableCull();
                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.enableWireframe();
                for (int x = plrChunkX - renderDistance; x < plrChunkX + renderDistance; x++) {
                    for (int z = plrChunkZ - renderDistance; z < plrChunkZ + renderDistance; z++) {
                        ChunkPos position = new ChunkPos(x, z);
                        LevelChunk chunk = level.getChunk(position);
                        if (chunk != null) {
                            Model model = new Model(CubeMesh.createCubeMesh("", 1, 1, 1, 1, 16, 256, 16, false));
                            model.setPosition(chunk.getChunkPos().x() * 16, 128, chunk.getChunkPos().z() * 16);
                            openCubes.gameRenderer.renderModel(camera, model, ShaderManager.getShader("default"));
                            model.getMesh().cleanup();
                        }
                    }
                }
                RenderSystem.disableWireframe();
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
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

    private void renderChunk(LevelChunk chunk, boolean transparent) {
        for (Map.Entry<String, ChunkMesh.ChunkModel> entry : (transparent ? chunk.getMesh().getTransparentModels() : chunk.getMesh().getOpaqueModels()).entrySet()) {
            for (Map.Entry<String, Model> entry2 : entry.getValue().models.entrySet()) {
                Model model = entry2.getValue();

                openCubes.gameRenderer.bindTexture(openCubes.atlas.getTexture());
                Shader shader = ShaderManager.getShader(entry.getKey());
                shader.bind(model);
                if (entry2.getKey().equals("all")) {
                    shader.setUniform("texture_offset", new Vec2(0,0));
                } else {
                    AtlasTexture texture = openCubes.atlas.getTexture(entry2.getKey());
                    if (texture != null) {
                        int atlasHeight = openCubes.atlas.getTexture().getHeight();
                        float pixelSize = 1f / atlasHeight;

                        int frameSize = texture.getHeight() / texture.getFrameAmount();

                        shader.setUniform("texture_offset", new Vec2(0,(frameSize * texture.getCurrentFrame()) * pixelSize));
                    }
                }
                openCubes.gameRenderer.renderModel(openCubes.gameRenderer.getMainCamera(), model, shader);
            }
        }
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
