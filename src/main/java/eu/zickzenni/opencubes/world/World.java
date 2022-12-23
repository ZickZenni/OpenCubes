package eu.zickzenni.opencubes.world;

import eu.zickzenni.opencubes.client.OpenCubes;
import eu.zickzenni.opencubes.client.util.GameSettings;
import eu.zickzenni.opencubes.client.world.Camera;
import eu.zickzenni.opencubes.entity.Entity;
import eu.zickzenni.opencubes.entity.PlayerEntity;
import eu.zickzenni.opencubes.world.chunk.Chunk;
import eu.zickzenni.opencubes.world.chunk.ChunkPosition;
import eu.zickzenni.opencubes.world.generation.OverworldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class World {
    private static final Logger logger = LogManager.getLogger("World");

    private final String name;
    private final int seed;

    private Dimension overworld;
    private ArrayList<Entity> entities = new ArrayList<>();

    public static World createWorld(String name) {
        return new World(name);
    }

    private World(String name) {
        this.name = name;
        //this.seed = 27032959;
        this.seed = (int) System.currentTimeMillis();
        this.overworld = new Dimension("overworld",  this, new OverworldGenerator(seed));
    }

    public void generate() {
        logger.info("Generating world...");
        logger.info("World seed: " + seed);
        overworld.generate();

        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            Entity pair = it.next();
            Vector3f position = pair.getSpawnLocation();
            pair.setPosition(position.x, position.y, position.z);
        }
    }

    public void update(float interval) {
        updatePlayerChunks();
    }

    public void tick() {
        overworld.tick();
        updateEntities();
    }

    public Entity addEntity(Entity entity) {
        Iterator<Entity> itC = entities.iterator();
        while (itC.hasNext()) {
            if (itC.next().getId() == entity.getId()) {
                throw new RuntimeException("Entity with id " + entity.getId() + " already exist!");
            }
        }
        logger.info("Adding entity with id " + entity.getId() + ": " + entity.getClass().getSimpleName());
        Vector3f position = entity.getSpawnLocation();
        entity.setPosition(position.x, position.y, position.z);
        entities.add(entity);
        return entity;
    }

    private List<ChunkPosition> chunksToRemove = new ArrayList<>();
    private List<ChunkPosition> newChunks = new ArrayList<>();

    private void updatePlayerChunks() {
        PlayerEntity player = OpenCubes.getInstance().getPlayer();
        Camera camera = OpenCubes.getInstance().getCamera();

        int plrChunkX = (int) camera.getPosition().x / 16;
        int plrChunkZ = (int) camera.getPosition().z / 16;
        chunksToRemove.clear();
        newChunks.clear();

        Iterator<Map.Entry<ChunkPosition, Chunk>> itC = player.getDimension().getChunks().entrySet().iterator();
        while (itC.hasNext()) {
            Map.Entry<ChunkPosition, Chunk> pair = itC.next();
            chunksToRemove.add(pair.getKey());
        }

        for (int x = plrChunkX - GameSettings.renderDistance; x < plrChunkX + GameSettings.renderDistance; x++) {
            for (int z = plrChunkZ - GameSettings.renderDistance; z < plrChunkZ + GameSettings.renderDistance; z++) {
                ChunkPosition chunkCoord = new ChunkPosition(x, z);
                if (!player.getDimension().getChunks().containsKey(chunkCoord)) {
                    try {
                        Chunk chunk = new Chunk(x, z, player.getDimension());
                        chunk.generateChunk();
                        player.getDimension().getChunks().put(chunkCoord, chunk);
                        newChunks.add(chunkCoord);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                chunksToRemove.remove(chunkCoord);
            }
        }

        for (ChunkPosition vector2i : chunksToRemove) {
            Chunk chunk = player.getDimension().getChunk(vector2i);
            player.getDimension().getChunks().remove(vector2i);
            chunk.destroy();
            chunk = null;
        }

        for (ChunkPosition newChunk : newChunks) {
            Chunk chunk = player.getDimension().getChunk(newChunk);
            chunk.generateMesh();

            ChunkPosition xN = new ChunkPosition(newChunk.getX() - 1, newChunk.getZ());
            if (!newChunks.contains(xN)) {
                Chunk neighbor = player.getDimension().getChunk(xN);
                if (neighbor != null) {
                    neighbor.generateMesh();
                }
            }
            ChunkPosition xP = new ChunkPosition(newChunk.getX() + 1, newChunk.getZ());
            if (!newChunks.contains(xP)) {
                Chunk neighbor = player.getDimension().getChunk(xP);
                if (neighbor != null) {
                    neighbor.generateMesh();
                }
            }
            ChunkPosition zN = new ChunkPosition(newChunk.getX(), newChunk.getZ() - 1);
            if (!newChunks.contains(zN)) {
                Chunk neighbor = player.getDimension().getChunk(zN);
                if (neighbor != null) {
                    neighbor.generateMesh();
                }
            }
            ChunkPosition zP = new ChunkPosition(newChunk.getX(), newChunk.getZ() + 1);
            if (!newChunks.contains(zP)) {
                Chunk neighbor = player.getDimension().getChunk(zP);
                if (neighbor != null) {
                    neighbor.generateMesh();
                }
            }
        }
    }

    private void updateEntities() {
        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            Entity pair = it.next();
            pair.update();
        }
    }

    public String getName() {
        return name;
    }

    public Dimension getOverworld() {
        return overworld;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
