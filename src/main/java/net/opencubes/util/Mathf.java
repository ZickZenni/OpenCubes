package net.opencubes.util;


public final class Mathf {
    public static float lerp(float a, float b, float t) {
        return a * (1.0f - t) + b * t;
    }

    public static int ceil(float value)
    {
        int i = (int)value;
        return value > (float)i ? i + 1 : i;
    }

    public static int ceil(double value)
    {
        int i = (int)value;
        return value > (double)i ? i + 1 : i;
    }

    public static boolean isPowerOfTwo(int n)
    {
        double v = Math.log(n) / Math.log(2);
        return (int)(Math.ceil(v))
                == (int)(Math.floor(v));
    }
}
