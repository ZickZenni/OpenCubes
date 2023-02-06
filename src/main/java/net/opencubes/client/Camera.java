package net.opencubes.client;

import net.opencubes.entity.Entity;
import net.opencubes.world.physics.Vec3;
import org.joml.Math;

public class Camera {
    private Vec3 position = new Vec3(0,0,0);

    private float yaw;
    private float pitch;

    private Entity entity;
    private boolean detached;

    public Camera(Entity entity, boolean detached) {
        this.entity = entity;
        this.detached = detached;
    }

    public Vec3 forward() {
        float yaw = Math.toRadians(this.yaw);
        float pitch = Math.toRadians(this.pitch + 90);

        Vec3 rayPosition = new Vec3(0,0,0);
        rayPosition.x = Math.cos(pitch) * Math.cos(yaw) * -1;
        rayPosition.y = Math.sin(yaw) * -1;
        rayPosition.z = Math.cos(yaw) * Math.sin(pitch) * -1;
        return rayPosition;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isDetached() {
        return detached;
    }

    public void setDetached(boolean detached) {
        this.detached = detached;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void update(float deltaTime) {
        if (entity != null && !detached) {
            position.lerp(entity.getPosition().clone().add(entity.eyePosition), 15 * deltaTime);
        }
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
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
}