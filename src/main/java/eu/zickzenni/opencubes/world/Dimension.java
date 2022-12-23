package eu.zickzenni.opencubes.world;

import eu.zickzenni.opencubes.block.Block;
import eu.zickzenni.opencubes.block.BlockSound;
import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.OpenCubes;
import eu.zickzenni.opencubes.client.util.GameSettings;
import eu.zickzenni.opencubes.world.chunk.Chunk;
import eu.zickzenni.opencubes.world.chunk.ChunkBlock;
import eu.zickzenni.opencubes.world.chunk.ChunkPosition;
import eu.zickzenni.opencubes.world.generation.WorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Dimension {
    private static final Logger logger = LogManager.getLogger("Dimension");

    private final String name;
    private final World world;
    private final WorldGenerator generator;
    private HashMap<ChunkPosition, Chunk> chunks = new HashMap<>();

    private byte skyLightLevel = 15;

    public Dimension(String name, World world, WorldGenerator generator) {
        this.name = name;
        this.world = world;
        this.generator = generator;
    }

    public void generate() {
        int radius = GameSettings.renderDistance;
        logger.info("Generating '" + name + "' with radius: " + radius);
        try {
            int amount = (radius * 2) * (radius * 2);
            float progress = 0;
            for (int cX = -radius; cX < radius; cX++) {
                for (int cZ = -radius; cZ < radius; cZ++) {
                    Chunk chunk = new Chunk(cX, cZ, this);
                    chunk.generateChunk();
                    chunks.put(new ChunkPosition(chunk.getX(), chunk.getZ()), chunk);
                    progress += 1f / amount;
                    logger.info("Generation progress: " + String.format(java.util.Locale.US, "%.2f", (progress * 100)) + "%");
                }
            }
            logger.info("Dimension generated!");
            updateAllChunks();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void render() {
        Iterator<Map.Entry<ChunkPosition, Chunk>> it = chunks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ChunkPosition, Chunk> pair = it.next();
            pair.getValue().renderChunk();
        }
    }

    public void tick() {
        tickAllChunks();
    }

    public boolean doesBlockExist(int x, int y, int z) {
        ChunkPosition position = toChunkPosition(x, z);
        Chunk chunk = getChunk(position);
        if (chunk == null)
            return false;
        Vector2i blockPos = toLocalBlockPosition(x, z);
        return chunk.doesBlockExist(blockPos.x, y, blockPos.y);
    }

    public ChunkBlock getBlock(int x, int y, int z) {
        if (!doesBlockExist(x, y, z))
            return null;

        ChunkPosition position = toChunkPosition(x, z);
        Chunk chunk = getChunk(position);
        if (chunk == null)
            return null;

        Vector2i blockPos = toLocalBlockPosition(x, z);
        return chunk.getBlockAt(blockPos.x, y, blockPos.y);
    }

    public void setBlock(int x, int y, int z, Block block) {
        ChunkPosition position = toChunkPosition(x, z);
        Chunk chunk = getChunk(position);
        if (chunk == null)
            return;
        Vector2i blockPos = toLocalBlockPosition(x, z);
        chunk.setBlock(blockPos.x, y, blockPos.y, block, false);
        updateNeighbors(chunk);
        chunk.generateMesh();
    }

    public void breakBlock(int x, int y, int z) {
        if (!doesBlockExist(x, y, z))
            return;
        ChunkBlock chunkBlock = getBlock(x,y,z);
        if (chunkBlock == null)
            return;
        Block block = Blocks.getBlock(chunkBlock.getBlockId());
        if (block == null)
            return;
        setBlock(x,y,z, Blocks.AIR);
        if (block.getSound() != BlockSound.NONE) {
            OpenCubes.getInstance().getSoundManager().playSound(block.getSound().getRandomDigSound());
        }
    }

    public boolean placeBlock(int x, int y, int z, Block block) {
        if (doesBlockExist(x, y, z))
            return false;
        setBlock(x,y,z, block);
        if (block.getSound() != BlockSound.NONE) {
            OpenCubes.getInstance().getSoundManager().playSound(block.getSound().getRandomDigSound());
        }
        return true;
    }

    private void updateNeighbors(Chunk chunk) {
        ChunkPosition xN = new ChunkPosition(chunk.getX() - 1, chunk.getZ());
        Chunk neighbor = getChunk(xN);
        if (neighbor != null) {
            neighbor.generateMesh();
        }
        ChunkPosition xP = new ChunkPosition(chunk.getX() + 1, chunk.getZ());
        neighbor = getChunk(xP);
        if (neighbor != null) {
            neighbor.generateMesh();
        }
        ChunkPosition zN = new ChunkPosition(chunk.getX(), chunk.getZ() - 1);
        neighbor = getChunk(zN);
        if (neighbor != null) {
            neighbor.generateMesh();
        }
        ChunkPosition zP = new ChunkPosition(chunk.getX(), chunk.getZ() + 1);
        neighbor = getChunk(zP);
        if (neighbor != null) {
            neighbor.generateMesh();
        }
    }

    private void updateAllChunks() {
        Iterator<Map.Entry<ChunkPosition, Chunk>> it = chunks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ChunkPosition, Chunk> pair = it.next();
            pair.getValue().generateMesh();
        }
    }

    private void tickAllChunks() {
        Iterator<Map.Entry<ChunkPosition, Chunk>> it = chunks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ChunkPosition, Chunk> pair = it.next();
            pair.getValue().tick();
        }
    }

    public World getWorld() {
        return world;
    }

    public WorldGenerator getGenerator() {
        return generator;
    }

    public HashMap<ChunkPosition, Chunk> getChunks() {
        return chunks;
    }

    public Chunk getChunk(ChunkPosition position) {
        return chunks.get(position);
    }

    public byte getSkyLightLevel() {
        return skyLightLevel;
    }

    public void setSkyLightLevel(byte skyLightLevel) {
        this.skyLightLevel = skyLightLevel;
        updateAllChunks();
    }

    private Vector2i toLocalBlockPosition(int x, int z) {
        return new Vector2i((16 + (x % 16)) % 16, (16 + (z % 16)) % 16);
    }

    private ChunkPosition toChunkPosition(int x, int z) {
        return new ChunkPosition(x < 0 ? ((x - 15) / 16) : (x / 16), z < 0 ? ((z - 15) / 16) : (z / 16));
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimension dimension = (Dimension) o;
        return Objects.equals(name, dimension.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}