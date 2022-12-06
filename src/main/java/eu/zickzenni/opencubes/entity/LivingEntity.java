package eu.zickzenni.opencubes.entity;

import eu.zickzenni.opencubes.world.Dimension;

public class LivingEntity extends Entity {
    private int health;
    private int maxHealth;
    private int gravity;

    public LivingEntity(int id, Dimension dimension) {
        super(id, dimension);
        health = 20;
        maxHealth = 20;
        gravity = 8;
    }

    @Override
    public void update(float interval) {
        movePosition(0, (8 * 0.07f) * interval, 0);
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