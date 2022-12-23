package eu.zickzenni.opencubes.world.chunk;

import eu.zickzenni.opencubes.block.Block;
import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.mesh.MeshBuilder;
import eu.zickzenni.opencubes.client.render.Model;
import eu.zickzenni.opencubes.client.render.RenderSystem;
import eu.zickzenni.opencubes.client.world.LightingEngine;
import eu.zickzenni.opencubes.world.Dimension;
import org.joml.Vector3f;

import java.util.Objects;

public class Chunk {
    public static final float LIGHT_LEVEL = 1 / 16f;

    private final int x;
    private final int z;
    private final Dimension dimension;
    private Model model;
    private boolean meshUpdate;

    private boolean destroyed;

    public MeshBuilder builder = new MeshBuilder();

    private ChunkBlock[][][] blocks = new ChunkBlock[16][256][16];
    private byte[][][] lightLevels = new byte[16][256][16];

    public Chunk(int x, int z, Dimension dimension) throws Exception {
        if (dimension == null)
            throw new Exception("Dimension cannot be null (Chunk::new)");

        this.x = x;
        this.z = z;
        this.dimension = dimension;
    }

    public boolean generated;
    public boolean meshGenerated;

    public void generateChunk() {
        if (!generated) {
            this.dimension.getGenerator().generate(this);
        }
    }

    public void tick() {
        if (meshUpdate) {
            if (generated) {
                LightingEngine.updateChunkLighting(this);
                if (ChunkMeshSystem.push(this)) {
                    meshUpdate = false;
                }
            }
        }
    }

    public void generateMesh() {
        meshUpdate = true;
    }

    public void buildMesh() {
        if (meshGenerated) {
            destroyMesh();
            model = new Model(builder.build(), 1, new Vector3f(x * 16 + 0.5f, 0, z * 16 + 0.5f));
            meshGenerated  = false;
            builder = null;
        }
    }

    public void renderChunk() {
        buildMesh();

        if (model == null)
            return;

        RenderSystem.renderModel(model);
    }

    /**
     * Sets the current block and regenerates the mesh (Only himself).
     * Should be handled by the world/dimension instead
     * @param x
     * @param y
     * @param z
     * @param block
     * @return
     */
    public void setBlock(int x, int y, int z, Block block, boolean update) {
        if (isDestroyed())
            return;
        if (!inChunk(x,y,z))
            return;
        if (block.getId() == Blocks.AIR.getId()) {
            blocks[x][y][z] = null;
        } else {
            ChunkBlock chunkBlock = new ChunkBlock(this,x,y,z,block);
            blocks[x][y][z] = chunkBlock;
        }
        if (update)
            generateMesh();
    }

    public boolean canRender(Block block) {
        return block.isTransparent();
    }

    public static boolean inChunk(int x, int y, int z) {
        return x > -1 && x < 16 && y > -1 && y < 256 && z > -1  && z < 16;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public ChunkBlock getBlockAt(int x, int y, int z) {
        if (!doesBlockExist(x,y,z))
            return null;
        return blocks[x][y][z];
    }

    public boolean doesBlockExist(int x, int y, int z) {
        if (isDestroyed())
            return false;
        if (!inChunk(x, y, z))
            return false;
        return blocks[x][y][z] != null;
    }

    public byte getBlockId(int x, int y, int z) {
        if (isDestroyed())
            return -1;
        if (!inChunk(x, y, z))
            return -1;
        return (byte) (blocks[x][y][z] != null ? blocks[x][y][z].getBlockId() : 0);
    }

    public byte getLightLevel(int x, int y, int z) {
        if (isDestroyed())
            return -1;
        if (!inChunk(x,y,z))
            return -1;
        return lightLevels[x][y][z];
    }

    public void setLightLevel(int lightLevel, int x, int y, int z) {
        if (!inChunk(x,y,z))
            return;
        if (lightLevel > 15)
            lightLevel = 15;
        if (lightLevel < 0)
            lightLevel = 0;
        lightLevels[x][y][z] = (byte) lightLevel;
    }

    public float calculateLightLevel(int x, int y, int z) {
        if (!inChunk(x,y,z))
            return LIGHT_LEVEL;
        if (blocks == null)
            return LIGHT_LEVEL;
        return LIGHT_LEVEL * getLightLevel(x,y,z) + LIGHT_LEVEL;
    }

    public int raycastY(int x, int z, int start, int end) {
        if (!inChunk(x, start, z))
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
            if (getBlockId(x,y,z) != Blocks.AIR.getId()) {
                return y + 1;
            }
        }
        return end;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void destroyMesh() {
        if (model != null) {
            model.getMesh().cleanup();
            model = null;
        }
    }

    public void destroy() {
        destroyMesh();
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
        Chunk chunk = (Chunk) o;
        return x == chunk.x && z == chunk.z && Objects.equals(dimension, chunk.dimension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, dimension);
    }
}