package eu.zickzenni.opencubes.world;

import eu.zickzenni.opencubes.block.Block;
import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.engine.mesh.MeshBuilder;
import eu.zickzenni.opencubes.client.engine.render.Model;
import eu.zickzenni.opencubes.client.engine.render.RenderSystem;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Objects;

public class Chunk {
    private static final float LIGHT_LEVEL = 1 / 16f;

    private final int x;
    private final int z;
    private final Dimension dimension;
    private Model model;

    public MeshBuilder builder = new MeshBuilder();

    private final byte[][][] blocks = new byte[16][256][16];
    private final byte[][][] lightLevels = new byte[16][256][16];

    public Chunk(int x, int z, Dimension dimension) throws Exception {
        if (dimension == null)
            throw new Exception("Dimension cannot be null (Chunk::new)");

        this.x = x;
        this.z = z;
        this.dimension = dimension;
    }

    public boolean generated;
    public boolean meshGenerated;

    public void generateChunk(boolean async) {
        if (!generated) {
            if (async) {
                new Thread(() -> {
                    this.dimension.getGenerator().generate(this);
                }).start();
            } else {
                this.dimension.getGenerator().generate(this);
            }
            generated = true;
        }
    }

    public void update(boolean meshUpdate) {
        if (meshUpdate) {
            if (generated)
                ChunkMeshSystem.push(this);
        }
    }

    public void renderChunk() {
        if (meshGenerated) {
            destroy();
            model = new Model(builder.build(), 1, new Vector3f(x * 16 + 0.5f, 0, z * 16 + 0.5f));
            meshGenerated  = false;
            builder = null;
        }

        if (model == null)
            return;

        RenderSystem.renderModel(model);
    }

    public void setBlock(Vector3i position, Block block) {
        if (!inChunk(position))
            return;
        blocks[position.x][position.y][position.z] = block.getId();
    }

    public boolean canRender(Block block) {
        return block.isAir() || block.isTransparent();
    }

    private boolean inChunk(Vector3i position) {
        return position.x >= 0 && position.x <= 15 && position.y >= 0 && position.y <= 255 && position.z >= 0 && position.z <= 15;
    }

    private boolean inChunk(int x, int y, int z) {
        return x >= 0 && x <= 15 && y >= 0 && y <= 255 && z >= 0 && z <= 15;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getBlockAt(Vector3i position) {
        if (!inChunk(position))
            return -1;
        return blocks[position.x][position.y][position.z];
    }

    public byte getBlockAt(int x, int y, int z) {
        if (!inChunk(x, y, z))
            return -1;
        return blocks[x][y][z];
    }

    public byte getLightLevelAt(int x, int y, int z) {
        if (!inChunk(x, y, z))
            return -1;
        return lightLevels[x][y][z];
    }

    public void setLightLevel(int level, int x, int y, int z) {
        if (level < 0 || level > 15) {
            return;
        }
        lightLevels[x][y][z] = (byte) level;
    }

    public float calculateLightLevel(int x, int y, int z) {
        return LIGHT_LEVEL * getLightLevelAt(x, y, z) + LIGHT_LEVEL;
    }

    public int raycastY(int x, int z, int start, int end) {
        if (!inChunk(new Vector3i(x, start, z)))
            return -1;

        if (start > 255)
            start = 255;
        if (end < 0)
            end = 0;
        if (start == end)
            return start;
        if (end > start) {
            int newEnd = start;
            start = end;
            end = newEnd;
        }
        for (int y = start; y >= end; y--) {
            if (getBlockAt(x, y, z) != Blocks.AIR.getId()) {
                return y + 1;
            }
        }
        return end;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public byte[][][] getBlocks() {
        return blocks;
    }

    public byte[][][] getLightLevels() {
        return lightLevels;
    }

    public void destroy() {
        if (model != null) {
            model.getMesh().cleanup();
            model = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chunk chunk = (Chunk) o;
        return x == chunk.x && z == chunk.z && Objects.equals(dimension, chunk.dimension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, dimension);
    }
}