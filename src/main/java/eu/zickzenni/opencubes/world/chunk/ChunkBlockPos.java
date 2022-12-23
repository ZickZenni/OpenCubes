package eu.zickzenni.opencubes.world.chunk;

import java.util.Objects;

public class ChunkBlockPos {
    private final Chunk chunk;
    private final byte x;
    private final byte y;
    private final byte z;

    public ChunkBlockPos(Chunk chunk, int x, int y, int z) {
        if (chunk == null) {
            throw new IllegalArgumentException("Chunk cannot be null!");
        }
        if (!Chunk.inChunk(x,y,z)) {
            throw new IllegalArgumentException("Position cannot be outside of chunk!");
        }
        this.chunk = chunk;
        this.x = (byte) x;
        this.y = (byte) y;
        this.z = (byte) z;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public byte getX() {
        return x;
    }

    public int getY() {
        return y + 128;
    }

    public byte getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkBlockPos that = (ChunkBlockPos) o;
        return x == that.x && y == that.y && z == that.z && Objects.equals(chunk, that.chunk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunk, x, y, z);
    }
}
