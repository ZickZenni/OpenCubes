package net.opencubes.world.level.chunk;


import net.opencubes.block.Block;

public class ChunkBlock {
    private final LevelChunk chunk;
    private final String blockName;
    private final byte x;
    private final byte y;
    private final byte z;

    public ChunkBlock(LevelChunk chunk, int x, int y, int z, Block block) {
        if (chunk == null) {
            throw new IllegalArgumentException("Chunk cannot be null!");
        }
        if (!chunk.inChunk(x,y,z)) {
            throw new IllegalArgumentException("Position cannot be outside of chunk!");
        }
        this.chunk = chunk;
        this.blockName = block.getName();
        this.x = (byte) x;
        this.y = (byte) y;
        this.z = (byte) z;
    }

    public LevelChunk getChunk() {
        return chunk;
    }

    public String getBlockName() {
        return blockName;
    }

    public byte getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public byte getZ() {
        return z;
    }

    public int getAbsoluteX() {
        return x + (chunk.getChunkPos().x() * 16);
    }

    public int getAbsoluteZ() {
        return z + (chunk.getChunkPos().z() * 16);
    }
}