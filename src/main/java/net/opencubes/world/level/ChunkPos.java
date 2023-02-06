package net.opencubes.world.level;

public class ChunkPos {
    private final int x;
    private final int z;

    public ChunkPos(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int x() {
        return x;
    }

    public int z() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkPos pos = (ChunkPos) o;
        return x == pos.x && z == pos.z;
    }

    @Override
    public int hashCode() {
        return x * 31 + z;
    }
}