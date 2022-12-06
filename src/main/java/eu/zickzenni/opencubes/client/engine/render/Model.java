package eu.zickzenni.opencubes.client.engine.render;

import eu.zickzenni.opencubes.client.engine.mesh.Mesh;
import org.joml.Vector3f;

public class Model {
    private final Mesh mesh;
    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;

    public Model(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0).mul(scale);
        scale = 0.5f;
        rotation = new Vector3f(0, 0, 0);
    }

    public Model(Mesh mesh, float scale, Vector3f position) {
        this.mesh = mesh;
        this.position = position.mul(scale);
        this.scale = scale;
        rotation = new Vector3f(0, 0, 0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return mesh;
    }
}