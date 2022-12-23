package eu.zickzenni.opencubes.entity;

import eu.zickzenni.opencubes.world.Dimension;
import eu.zickzenni.opencubes.world.chunk.ChunkPosition;
import org.joml.Vector3f;

public class LivingEntity extends Entity {
    private int health;
    private int maxHealth;
    private int gravity;

    public LivingEntity(int id, Dimension dimension) {
        super(id, dimension);
        health = 20;
        maxHealth = 20;
        gravity = 20;
    }

    @Override
    public Vector3f getSpawnLocation() {
        return new Vector3f(0, getDimension().getChunk(new ChunkPosition(0,0)).raycastY(0, 0, 255, 50) + 10, 0);
    }

    @Override
    public void update() {
        movePosition(0, -(gravity * 0.7f), 0);
        if (getDimension().getChunk(new ChunkPosition((int)getPosition().x / 16, (int)getPosition().z / 16)).doesBlockExist(Math.floorMod((int)getPosition().x, 16), (int)getPosition().y, Math.floorMod((int)getPosition().z, 16))) {
            setPosition(getPosition().x, Math.round(getPosition().y), getPosition().z);
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getGravity() {
        return gravity;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }
}