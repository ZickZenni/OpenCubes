package eu.zickzenni.opencubes.world.chunk;

import eu.zickzenni.opencubes.block.Block;

public class ChunkBlock {
    private final Chunk chunk;
    private final byte x;
    private final byte y;
    private final byte z;

    private final byte blockId;

    public ChunkBlock(Chunk chunk, int x, int y, int z, Block block) {
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
        this.blockId = block.getId();
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

    public byte getBlockId() {
        return blockId;
    }
}
