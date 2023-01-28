package net.opencubes.entity;

import net.opencubes.world.physics.Vec2;
import net.opencubes.world.physics.Vec3;

public abstract class Entity {
    private final int id;
    private Vec3 position;
    private float yaw;
    private float pitch;

    protected float prevX;
    protected float prevY;
    protected float prevZ;

    public Vec3 eyePosition;

    public Entity(int id, Vec3 position, float yaw, float pitch) {
        this.id = id;
        this.position = position;
        this.prevX = position.x;
        this.prevY = position.y;
        this.prevZ = position.z;
        this.yaw = 0;
        this.pitch = 0;
        this.eyePosition = new Vec3(0, 0, 0);
    }

    public void tick() {
    }

    public abstract Vec2 getBoundingBox();

    public void movePosition(float x, float y, float z) {
        setPosition(new Vec3(position.x + x, position.y + y, position.z + z));
    }

    public void moveRotation(float yaw, float pitch) {
        this.setYaw(this.yaw + yaw);
        this.setPitch(this.pitch + pitch);
    }

    public int getId() {
        return id;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        prevX = this.position.x;
        prevY = this.position.y;
        prevZ = this.position.z;
        this.position = position;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getPrevX() {
        return prevX;
    }

    public float getPrevY() {
        return prevY;
    }

    public float getPrevZ() {
        return prevZ;
    }
}
