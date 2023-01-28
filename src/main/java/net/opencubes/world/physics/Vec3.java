package net.opencubes.world.physics;

import org.joml.Math;

import java.util.Objects;

public class Vec3 {
    public float x;
    public float y;
    public float z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 clone() {
        return new Vec3(this.x, this.y, this.z);
    }

    public Vec3 mult(float v) {
        this.x *= v;
        this.y *= v;
        this.z *= v;
        return this;
    }

    public Vec3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vec3 add(Vec3 v) {
        return add(v.x, v.y, v.z);
    }

    public Vec3 sub(float x, float y, float z) {
        this.x = this.x - x;
        this.y = this.y - y;
        this.z = this.z - z;
        return this;
    }

    public Vec3 sub(Vec3 v) {
        return sub(v.x, v.y, v.z);
    }

    public Vec3 lerp(Vec3 other, float t) {
        return lerp(other, t, this);
    }

    public Vec3 lerp(Vec3 other, float t, Vec3 dest) {
        dest.x = Math.fma(other.x - x, t, x);
        dest.y = Math.fma(other.y - y, t, y);
        dest.z = Math.fma(other.z - z, t, z);
        return dest;
    }

    public Vec3 normalize() {
        float scalar = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, z * z)));
        this.x = this.x * scalar;
        if (Float.isNaN(x)) {
            x = 0;
        }
        this.y = this.y * scalar;
        if (Float.isNaN(y)) {
            y = 0;
        }
        this.z = this.z * scalar;
        if (Float.isNaN(z)) {
            z = 0;
        }
        return this;
    }

    public double distance(Vec3 v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        float dz = this.z - v.z;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, dz * dz)));
    }

    public Vec3 neg() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return  this;
    }

    public Vec3 reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3 Vec3 = (Vec3) o;
        return x == Vec3.x && y == Vec3.y && z == Vec3.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vec3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
