package net.opencubes.util;

import net.opencubes.world.physics.Vec3;
import org.joml.Math;

public class Ray {
    private final Vec3 position;
    private final Vec3 rotation;
    private final float range;
    private final int steps;
    private int currentStep;

    public Ray(Vec3 position, Vec3 rotation, float range, int steps) {
        this.position = position;
        this.rotation =  rotation;
        this.range = range;
        this.steps = steps;
    }

    public Vec3 step() {
        if (currentStep >= steps)
            return calculateStep(range);
        float z = (range / steps) * currentStep;
        this.currentStep++;
        return calculateStep(z);
    }

    private Vec3 calculateStep(float z) {
        float yaw = Math.toRadians(rotation.y + 90);
        float pitch = Math.toRadians(rotation.x);

        Vec3 rayPosition = position.clone();
        rayPosition.x += Math.cos(yaw) * Math.cos(pitch) * -z;
        rayPosition.y += Math.sin(pitch) * -z;
        rayPosition.z += Math.cos(pitch) * Math.sin(yaw) * -z;
        return rayPosition;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec3 getRotation() {
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