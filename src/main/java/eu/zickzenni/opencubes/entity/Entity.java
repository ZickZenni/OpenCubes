package eu.zickzenni.opencubes.entity;

import eu.zickzenni.opencubes.world.Dimension;
import eu.zickzenni.opencubes.world.chunk.Chunk;
import eu.zickzenni.opencubes.world.chunk.ChunkPosition;
import org.joml.Math;
import org.joml.Vector3f;

public class Entity {
    private final int id;
    protected Vector3f position;
    protected Vector3f rotation;
    private Dimension dimension;

    public Entity(int id, Dimension dimension) {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.id = id;
        this.dimension = dimension;
    }

    public Vector3f getSpawnLocation() {
        return getDefaultSpawnLocation();
    }

    public final Vector3f getDefaultSpawnLocation() {
        Chunk chunk = this.getDimension().getChunk(new ChunkPosition(0,0));
        return new Vector3f(0, chunk.raycastY(0, 0, 255,  0), 0);
    }

    public void update() {
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ != 0) {
            position.x += Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if (offsetX != 0) {
            position.x += Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    public int getId() {
        return id;
    }
}