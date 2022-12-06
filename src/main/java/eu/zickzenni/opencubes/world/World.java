package eu.zickzenni.opencubes.world;

import eu.zickzenni.opencubes.entity.CameraEntity;
import eu.zickzenni.opencubes.entity.Entity;
import eu.zickzenni.opencubes.util.SimpleProfiler;
import eu.zickzenni.opencubes.world.generation.OverworldGenerator;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class World {
    private String name;

    private Dimension overworld;
    private ArrayList<Entity> entities = new ArrayList<>();

    private static CameraEntity camera;

    private int renderDistance = 8;

    public static World createWorld(String name) {
        return new World(name);
    }

    private World(String name) {
        int seed = (int) System.currentTimeMillis();

        this.name = name;
        this.overworld = new Dimension("overworld",  this, new OverworldGenerator(seed));
        camera = new CameraEntity(0, overworld);
        entities.add(camera);
    }

    public void generate() {
        System.out.println("Generating world...");
        SimpleProfiler.start("WORLD_GENERATION");

        overworld.generate();

        SimpleProfiler.stop("WORLD_GENERATION");

        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            Entity pair = it.next();
            Vector3f position = pair.getSpawnLocation();
            pair.setPosition(position.x, position.y, position.z);
        }
    }

    private List<ChunkPosition> chunksToRemove = new ArrayList<>();
    private List<ChunkPosition> newChunks = new ArrayList<>();

    public void update(float interval) {
        int plrChunkX = (int) camera.getPosition().x / 16;
        int plrChunkZ = (int) camera.getPosition().z / 16;
        chunksToRemove.clear();
        newChunks.clear();

        Iterator<Map.Entry<ChunkPosition, Chunk>> itC = camera.getDimension().getChunks().entrySet().iterator();
        while (itC.hasNext()) {
            Map.Entry<ChunkPosition, Chunk> pair = itC.next();
            chunksToRemove.add(pair.getKey());
        }

        for (int x = plrChunkX - renderDistance; x < plrChunkX + renderDistance; x++) {
            for (int z = plrChunkZ - renderDistance; z < plrChunkZ + renderDistance; z++) {
                ChunkPosition chunkCoord = new ChunkPosition(x, z);
                if (!camera.getDimension().getChunks().containsKey(chunkCoord)) {
                    try {
                        Chunk chunk = new Chunk(x, z, camera.getDimension());
                        chunk.generateChunk(true);
                        camera.getDimension().getChunks().put(chunkCoord, chunk);
                        newChunks.add(chunkCoord);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                chunksToRemove.remove(chunkCoord);
            }
        }

        for (ChunkPosition vector2i : chunksToRemove) {
            Chunk chunk = camera.getDimension().getChunk(vector2i);
            camera.getDimension().getChunks().remove(vector2i);
            chunk.destroy();
        }

        for (ChunkPosition newChunk : newChunks) {
            Chunk chunk = camera.getDimension().getChunk(newChunk);
            chunk.update(true);

            ChunkPosition xN = new ChunkPosition(newChunk.getX() - 1, newChunk.getZ());
            if (!newChunks.contains(xN)) {
                Chunk neighbor = camera.getDimension().getChunk(xN);
                if (neighbor != null) {
                    neighbor.update(true);
                }
            }
            ChunkPosition xP = new ChunkPosition(newChunk.getX() + 1, newChunk.getZ());
            if (!newChunks.contains(xP)) {
                Chunk neighbor = camera.getDimension().getChunk(xP);
                if (neighbor != null) {
                    neighbor.update(true);
                }
            }
            ChunkPosition zN = new ChunkPosition(newChunk.getX(), newChunk.getZ() - 1);
            if (!newChunks.contains(zN)) {
                Chunk neighbor = camera.getDimension().getChunk(zN);
                if (neighbor != null) {
                    neighbor.update(true);
                }
            }
            ChunkPosition zP = new ChunkPosition(newChunk.getX(), newChunk.getZ() + 1);
            if (!newChunks.contains(zP)) {
                Chunk neighbor = camera.getDimension().getChunk(zP);
                if (neighbor != null) {
                    neighbor.update(true);
                }
            }
        }

        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            Entity pair = it.next();
            pair.update(interval);
        }
    }

    public String getName() {
        return name;
    }

    public int getRenderDistance() {
        return renderDistance;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public static CameraEntity getCamera() {
        return camera;
    }
}
