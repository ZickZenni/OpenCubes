package net.opencubes.client.vertex;

import net.opencubes.world.physics.Vec3;

public class Model {
    private final Mesh mesh;
    private Vec3 position;
    private float scale;
    private Vec3 rotation;

    public Model(Mesh mesh) {
        this.mesh = mesh;
        this.position = new Vec3(0,0,0);
        this.scale = 1f;
        this.rotation = new Vec3(0,0,0);
    }

    public Model(Mesh mesh, float scale, Vec3 position) {
        this.mesh = mesh;
        this.position = position;
        this.scale = scale;
        this.rotation = new Vec3(0,0,0);
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void addPosition(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vec3 getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void addRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public Mesh getMesh() {
        return mesh;
    }
}