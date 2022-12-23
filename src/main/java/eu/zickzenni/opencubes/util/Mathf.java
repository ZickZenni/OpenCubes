package eu.zickzenni.opencubes.util;

import org.joml.Vector3f;

public final class Mathf {
    public static float lerp(float a, float b, float t) {
        return a * (1.0f - t) + b * t;
    }

    public static boolean isPowerOfTwo(int n)
    {
        double v = Math.log(n) / Math.log(2);
        return (int)(Math.ceil(v))
                == (int)(Math.floor(v));
    }

    public  static Vector3f multiply(Vector3f a, float t) {
        return new Vector3f(a.x * t,a.y * t,a.z * t);
    }

    public static Vector3f multiply(Vector3f a, Vector3f b, float t) {
        float x = 0;
        float y = 0;
        float z = 0;
        x = a.x * b.x * t;
        y = a.y * b.y * t;
        z = a.z * b.z * t;
        return new Vector3f(x,y,z);
    }
}
