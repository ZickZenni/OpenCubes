package eu.zickzenni.opencubes.util;

import org.joml.Math;
import org.joml.Vector3f;

public class Ray {
    private Vector3f position;
    private Vector3f rotation;
    private float range;
    private int steps;
    private int currentStep;

    public Ray(Vector3f position, Vector3f rotation, float range, int steps) {
        this.position = position;
        this.rotation =  rotation;
        this.range = range;
        this.steps = steps;
    }

    public Vector3f step() {
        if (currentStep >= steps)
            return calculateStep(range);
        float z = (range / steps) * currentStep;
        this.currentStep++;
        return calculateStep(z);
    }

    private Vector3f calculateStep(float z) {
        float yaw = Math.toRadians(rotation.y + 90);
        float pitch = Math.toRadians(rotation.x);

        Vector3f rayPosition = new Vector3f(position);
        rayPosition.x += Math.cos(yaw) * Math.cos(pitch) * -z;
        rayPosition.y += Math.sin(pitch) * -z;
        rayPosition.z += Math.cos(pitch) * Math.sin(yaw) * -z;
        return rayPosition;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getRange() {
        return range;
    }

    public int getSteps() {
        return steps;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public boolean isAtTheEnd() {
        return currentStep >= steps;
    }
}
