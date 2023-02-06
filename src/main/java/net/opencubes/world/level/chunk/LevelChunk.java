package net.opencubes.world.level.chunk;

import net.opencubes.block.Block;
import net.opencubes.client.level.chunk.ChunkMesh;
import net.opencubes.world.level.ChunkPos;
import net.opencubes.world.level.Level;
import net.opencubes.world.physics.Vec2;
import net.opencubes.world.physics.Vec3;

import java.util.Objects;

public class LevelChunk {
    protected final Level level;

    private final Vec2 centerPoint;
    private final ChunkPos chunkPos;
    private boolean loaded;
    private boolean destroyed;

    private ChunkMesh mesh;
    private boolean shouldGenerateMesh;

    private ChunkBlock[][][] blocks = new ChunkBlock[16][256][16];
    private byte[][][] lightLevels = new byte[16][256][16];

    public LevelChunk(Level level, ChunkPos chunkPos) {
        this.level = level;
        this.chunkPos = chunkPos;
        this.centerPoint = new Vec2(chunkPos.x() + 8, chunkPos.z() + 8);
        this.loaded = true;
        this.mesh = new ChunkMesh(this);
    }

    public void tick() {

    }

    public void tickMesh() {
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

    public boolean shouldGenerateMesh() {
        return shouldGenerateMesh;
    }

    public void destroy() {
        blocks = null;
        lightLevels = null;
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public double getDistance(Vec2 position) {
        return position.distance(centerPoint);
    }

    public double getDistance(Vec3 position) {
        Vec2 cPosition = new Vec2(position.x, position.z);
        return cPosition.distance(centerPoint);
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