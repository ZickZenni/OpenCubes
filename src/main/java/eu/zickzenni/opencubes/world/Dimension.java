package eu.zickzenni.opencubes.world;

import eu.zickzenni.opencubes.world.generation.WorldGenerator;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Dimension {
    private final String name;
    private final World world;
    private final WorldGenerator generator;
    private HashMap<ChunkPosition, Chunk> chunks = new HashMap<>();

    public Dimension(String name, World world, WorldGenerator generator) {
        this.name = name;
        this.world = world;
        this.generator = generator;
    }

    public void generate() {
        int radius = world.getRenderDistance();
        try {
            for (int cX = -radius; cX < radius; cX++) {
                for (int cZ = -radius; cZ < radius; cZ++) {
                    Chunk chunk = new Chunk(cX, cZ, this);
                    chunk.generateChunk(false);
                    chunks.put(new ChunkPosition(chunk.getX(), chunk.getZ()), chunk);
                }
            }
            updateAllChunks(true);
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

    public void updateAllChunks(boolean renewMesh) {
        Iterator<Map.Entry<ChunkPosition, Chunk>> it = chunks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ChunkPosition, Chunk> pair = it.next();
            pair.getValue().update(renewMesh);
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