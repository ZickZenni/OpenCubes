package net.opencubes.world.physics;

import org.joml.Math;

import java.util.Objects;

public class Vec2 {
    public static final Vec2 ZERO = new Vec2(0,0);

    public float x;
    public float y;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 clone() {
        return new Vec2(this.x, this.y);
    }

    public Vec2 mult(float v) {
        this.x *= v;
        this.y *= v;
        return this;
    }

    public Vec2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2 add(Vec2 v) {
        return add(v.x, v.y);
    }

    public Vec2 sub(float x, float y) {
        this.x = this.x - x;
        this.y = this.y - y;
        return this;
    }

    public Vec2 sub(Vec3 v) {
        return sub(v.x, v.y);
    }

    public Vec2 normalize() {
        float scalar = Math.invsqrt(x * x + y * y);
        this.x = this.x * scalar;
        if (Float.isNaN(x)) {
            x = 0;
        }
        this.y = this.y * scalar;
        if (Float.isNaN(y)) {
            y = 0;
        }
        return this;
    }

    public double distance(Vec2 v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Vec2 neg() {
        this.x = -this.x;
        this.y = -this.y;
        return  this;
    }

    public Vec2 reset() {
        this.x = 0;
        this.y = 0;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2 v = (Vec2) o;
        return x == v.x && y == v.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
