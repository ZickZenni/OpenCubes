package net.opencubes.world.level;

import net.opencubes.block.Block;
import net.opencubes.block.BlockRegistry;
import net.opencubes.block.BlockSound;
import net.opencubes.client.OpenCubes;
import net.opencubes.client.renderer.LevelRenderer;
import net.opencubes.entity.Entity;
import net.opencubes.entity.player.LocalPlayer;
import net.opencubes.util.FastNoiseLite;
import net.opencubes.world.level.chunk.ChunkBlock;
import net.opencubes.world.level.chunk.LevelChunk;
import net.opencubes.world.level.lighting.LevelLightingEngine;
import net.opencubes.world.physics.Vec3;
import org.joml.Vector2i;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Level {
    private static final int MESH_GENERATION_PER_FRAME = 3;
    private int generations = 0;

    public final LevelLightingEngine lightingEngine;
    protected final HashMap<ChunkPos, LevelChunk> chunks;

    private final FastNoiseLite noise = new FastNoiseLite();

    protected final ArrayList<Entity> entities = new ArrayList<>();

    public Level() {
        this.chunks = new HashMap<>();
        this.lightingEngine = new LevelLightingEngine(this);
        LevelGenerationSystem.start(this);
    }

    private ArrayList<ChunkPos> chunksToRemove = new ArrayList<>();

    public void tick() {
        LocalPlayer player = OpenCubes.getInstance().player;
        if (player != null) {
            int plrChunkX = (int) player.getPosition().x / 16;
            int plrChunkZ = (int) player.getPosition().z / 16;

            for (int x = plrChunkX - LevelRenderer.renderDistance; x < plrChunkX + LevelRenderer.renderDistance; x++) {
                for (int z = plrChunkZ - LevelRenderer.renderDistance; z < plrChunkZ + LevelRenderer.renderDistance; z++) {
                    ChunkPos position = new ChunkPos(x, z);
                    if (!chunks.containsKey(position)) {
                        try {
                            LevelGenerationSystem.addChunk(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    chunksToRemove.remove(position);
                }
            }

            chunksToRemove.clear();

            try {
                Iterator<Map.Entry<ChunkPos, LevelChunk>> itC = chunks.entrySet().iterator();
                while (itC.hasNext()) {
                    Map.Entry<ChunkPos, LevelChunk> pair = itC.next();
                    chunksToRemove.add(pair.getKey());
                }
            } catch (Exception ignore) {}

            for (int x = plrChunkX - LevelRenderer.renderDistance; x < plrChunkX + LevelRenderer.renderDistance; x++) {
                for (int z = plrChunkZ - LevelRenderer.renderDistance; z < plrChunkZ + LevelRenderer.renderDistance; z++) {
                    chunksToRemove.remove(new ChunkPos(x, z));
                }
            }

            for (ChunkPos position : chunksToRemove) {
                LevelChunk chunk = getChunk(position);
                if (chunk != null) {
                    chunk.destroy();
                }
                chunks.remove(position);
            }
        }
        Iterator<Map.Entry<ChunkPos, LevelChunk>> it = chunks.entrySet().iterator();
        try {
            while (it.hasNext()) {
                Map.Entry<ChunkPos, LevelChunk> pair = it.next();
                LevelChunk chunk = pair.getValue();
                if (chunk.isLoaded()) {
                    chunk.tick();
                }
            }
        } catch (ConcurrentModificationException ignored
        ) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        tickEntities();
        LevelGenerationSystem.tick();
    }

    public void tickFrame() {
        generations = 0;
        Iterator<Map.Entry<ChunkPos, LevelChunk>> it = chunks.entrySet().iterator();
        try {
            while (it.hasNext()) {
                Map.Entry<ChunkPos, LevelChunk> pair = it.next();
                LevelChunk chunk = pair.getValue();
                if (chunk.isLoaded()) {
                    if (chunk.shouldGenerateMesh()) {
                        if (generations < MESH_GENERATION_PER_FRAME) {
                            chunk.tickMesh();
                            generations++;
                        }
                    }
                }
            }
        } catch (ConcurrentModificationException ignored) {

        }
    }

    public void generateChunk(ChunkPos position) {
        if (getChunk(position) != null) {
            return;
        }
        LevelChunk chunk = addChunk(position);

        generateTerrain(chunk);

        //this.lightingEngine.computeChunk(chunk);
        chunk.setGenerateMesh();
        updateNeighbors(chunk.getChunkPos());
    }

    private void generateTerrain(LevelChunk chunk) {
        ChunkPos position = chunk.getChunkPos();
        for (int xB = 0; xB < 16; xB++) {
            for (int zB = 0; zB < 16; zB++) {
                int height = (int) (noise.GetNoise(xB + position.x() * 16, zB + position.z() * 16) * 10) + 70;
                chunk.setBlockAt(xB, height, zB, BlockRegistry.GRASS_BLOCK);
                for (int y = height - 1; y > 0; y--) {
                    if (y >= height - 4) {
                        chunk.setBlockAt(xB, y, zB, BlockRegistry.DIRT);
                    } else {
                        chunk.setBlockAt(xB, y, zB, BlockRegistry.STONE);
                    }
                }
            }
        }
    }

    protected void tickEntities() {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.tick();
        }
    }

    public void breakBlock(int x, int y, int z) {
        ChunkBlock currentBlock = getBlock(x, y, z);
        if (currentBlock != null) {
            Block block = BlockRegistry.getBlock(currentBlock.getBlockName());
            if (block.getSound() != BlockSound.NONE) {
                OpenCubes.getInstance().getSoundManager().playSound(block.getSound().getRandomDigSound());
            }
        }
        setBlock(x, y, z, null);
    }

    public void placeBlock(int x, int y, int z, Block block) {
        setBlock(x,y,z, block);
        if (block.getSound() != BlockSound.NONE) {
            OpenCubes.getInstance().getSoundManager().playSound(block.getSound().getRandomDigSound());
        }
    }

    private void updateNeighbors(ChunkPos position) {
        for (int x = -1; x < 1; x++) {
            updateChunk(new ChunkPos(position.x() + x, position.z() + 1));
        }
        for (int x = -1; x < 1; x++) {
            updateChunk(new ChunkPos(position.x() + x, position.z() - 1));
        }
        updateChunk(new ChunkPos(position.x() + 1, position.z()));
        updateChunk(new ChunkPos(position.x() - 1, position.z()));
    }

    private void updateNeighbors(int x, int y, int z) {
        ChunkPos position = toChunkPosition(x, z);
        LevelChunk chunk = getChunk(position);
        if (chunk == null)
            return;

        Vector2i blockPos = toLocalBlockPosition(x, z);
        if (blockPos.x == 15) {
            updateChunk(new ChunkPos(position.x() + 1, position.z()));
        } else if (blockPos.x == 0) {
            updateChunk(new ChunkPos(position.x() - 1, position.z()));
        }
        if (blockPos.y == 15) {
            updateChunk(new ChunkPos(position.x(), position.z() + 1));
        } else if (blockPos.y == 0) {
            updateChunk(new ChunkPos(position.x(), position.z() - 1));
        }
        if (blockPos.x == 15 && blockPos.y == 15) {
            updateChunk(new ChunkPos(position.x() + 1, position.z() + 1));
        } else if (blockPos.x == 0 && blockPos.y == 0) {
            updateChunk(new ChunkPos(position.x() - 1, position.z() - 1));
        } else if (blockPos.x == 15 && blockPos.y == 0) {
            updateChunk(new ChunkPos(position.x() + 1, position.z() - 1));
        } else if (blockPos.x == 0 && blockPos.y == 15) {
            updateChunk(new ChunkPos(position.x() - 1, position.z() + 1));
        }
    }

    private void updateChunk(ChunkPos pos) {
        LevelChunk chunk = getChunk(pos);
        if (chunk != null)  {
            chunk.setGenerateMesh();
        }
    }

    public Entity addEntity(Class<? extends Entity> entity, Vec3 position, float yaw, float pitch) {
        int id = entities.size() + 1;
        try {
            Entity newEntity = entity.getDeclaredConstructor(int.class, Vec3.class, float.class, float.class).newInstance(id, position, yaw, pitch);
            entities.add(newEntity);
            return newEntity;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LevelChunk addChunk(ChunkPos pos) {
        LevelChunk chunk = new LevelChunk(this, pos);
        chunks.put(pos, chunk);
        return chunk;
    }

    public LevelChunk getChunk(ChunkPos chunkPos) {
        return chunks.getOrDefault(chunkPos, null);
    }

    public ChunkBlock getBlock(int x, int y, int z) {
        ChunkPos position = toChunkPosition(x, z);
        LevelChunk chunk = getChunk(position);
        if (chunk == null)
            return null;

        Vector2i blockPos = toLocalBlockPosition(x, z);
        return chunk.getBlockAt(blockPos.x, y, blockPos.y);
    }

    public void setBlock(int x, int y, int z, Block block) {
        ChunkPos position = toChunkPosition(x, z);
        LevelChunk chunk = getChunk(position);
        if (chunk == null)
            return;

        Vector2i blockPos = toLocalBlockPosition(x, z);
        chunk.setBlockAt(blockPos.x, y, blockPos.y, block);
        chunk.setGenerateMesh();
        updateNeighbors(x,y,z);
    }

    protected ChunkPos toChunkPosition(int x, int z) {
        return new ChunkPos(x < 0 ? ((x - 15) / 16) : (x / 16), z < 0 ? ((z - 15) / 16) : (z / 16));
    }

    private Vector2i toLocalBlockPosition(int x, int z) {
        return new Vector2i((16 + (x % 16)) % 16, (16 + (z % 16)) % 16);
    }

    public int getSkyLightLevel() {
        return 15;
    }

    public Map<ChunkPos, LevelChunk> getChunks() {
        return Collections.unmodifiableMap(chunks);
    }
}