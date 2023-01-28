package net.opencubes.world.physics;

import org.joml.Math;

import java.util.Objects;

public class Vec4 {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4 clone() {
        return new Vec4(this.x, this.y, this.z, this.w);
    }

    public Vec4 mult(float v) {
        this.x *= v;
        this.y *= v;
        this.z *= v;
        this.w *= v;
        return this;
    }

    public Vec4 add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }

    public Vec4 add(Vec4 v) {
        return add(v.x, v.y, v.z, v.w);
    }

    public Vec4 sub(float x, float y, float z, float w) {
        this.x = this.x - x;
        this.y = this.y - y;
        this.z = this.z - z;
        this.w = this.w - w;
        return this;
    }

    public Vec4 sub(Vec4 v) {
        return sub(v.x, v.y, v.z, v.w);
    }

    public Vec4 normalize() {
        float invLength = 1.0f / length();
        this.x = x * invLength;
        this.y = y * invLength;
        this.z = z * invLength;
        this.w = w * invLength;
        return this;
    }

    public double distance(Vec4 v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        float dz = this.z - v.z;
        float dw = this.w - v.w;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, Math.fma(dz, dz, dw * dw))));
    }

    public Vec4 neg() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return  this;
    }

    public Vec4 reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec4 vec4 = (Vec4) o;
        return Float.compare(vec4.x, x) == 0 && Float.compare(vec4.y, y) == 0 && Float.compare(vec4.z, z) == 0 && Float.compare(vec4.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public String toString() {
        return "Vec4{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }

    public float length() {
        return Math.sqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))));
    }
}