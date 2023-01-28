package net.opencubes.world.level.chunk;

import net.opencubes.block.Block;
import net.opencubes.client.level.chunk.ChunkMesh;
import net.opencubes.world.level.ChunkPos;
import net.opencubes.world.level.Level;

import java.util.Objects;

public class LevelChunk {
    protected final Level level;
    private final ChunkPos chunkPos;
    private boolean loaded;
    private boolean destroyed;

    private final ChunkMesh mesh;
    private boolean shouldGenerateMesh;

    private ChunkBlock[][][] blocks = new ChunkBlock[16][256][16];
    private byte[][][] lightLevels = new byte[16][256][16];

    public LevelChunk(Level level, ChunkPos chunkPos) {
        this.level = level;
        this.chunkPos = chunkPos;
        this.loaded = true;
        this.mesh = new ChunkMesh(this);
    }

    public void tick() {
        if (this.shouldGenerateMesh) {
            this.level.lightingEngine.computeChunk(this);
            this.mesh.computeMeshes();
            this.shouldGenerateMesh = false;
        }
    }

    public void setBlockAt(int x, int y, int z, Block block) {
        if (isDestroyed())
            return;
        if (inChunk(x, y, z) && blocks.length != 0) {
            if (block == null) {
                blocks[x][y][z] = null;
            } else {
                blocks[x][y][z] = new ChunkBlock(this, x, y, z, block);
            }
        }
    }

    public ChunkBlock getBlockAt(int x, int y, int z) {
        if (isDestroyed())
            return null;
        if (!inChunk(x, y, z))
            return null;
        return blocks[x][y][z];
    }

    public void setLightLevel(int x, int y, int z, int lightLevel) {
        if (isDestroyed())
            return;
        if (inChunk(x, y, z)) {
            lightLevels[x][y][z] = (byte) lightLevel;
        }
    }

    public byte getLightLevel(int x, int y, int z) {
        if (isDestroyed())
            return 0;
        if (inChunk(x, y, z)) {
            return lightLevels[x][y][z];
        }
        return 0;
    }

    public byte[][][] getLightLevels() {
        return lightLevels;
    }

    public boolean inChunk(int x, int y, int z) {
        return x > -1 && x < 16 && y > -1 && y < 256 && z > -1 && z < 16;
    }

    public Level getLevel() {
        return level;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public ChunkMesh getMesh() {
        return mesh;
    }

    public void setGenerateMesh() {
        this.shouldGenerateMesh = true;
    }

    public void destroy() {
        blocks = new ChunkBlock[0][0][0];
        lightLevels = new byte[0][0][0];
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelChunk that = (LevelChunk) o;
        return Objects.equals(chunkPos, that.chunkPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkPos);
    }
}