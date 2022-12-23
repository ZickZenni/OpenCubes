package eu.zickzenni.opencubes.client.util;

import org.joml.Vector3f;

import java.util.ArrayList;

public final class Converter {
    private Converter() {}

    public static float[] convertVector3f(ArrayList<Vector3f> array) {
        final float[] arr = new float[array.size() * 3];
        int index = 0;
        for (Vector3f vector3f : array) {
            arr[index++] = vector3f.x;
            arr[index++] = vector3f.y;
            arr[index++] = vector3f.z;
        }
        return arr;
    }

    public static float[] convertFloat(ArrayList<Float> array) {
        final float[] arr = new float[array.size()];
        int index = 0;
        for (final Float value : array) {
            arr[index++] = value;
        }
        return arr;
    }

    public static int[] convertInt(ArrayList<Integer> array) {
        final int[] arr = new int[array.size()];
        int index = 0;
        for (final Integer value : array) {
            arr[index++] = value;
        }
        return arr;
    }

    public static byte[] convertByte(ArrayList<Byte> array) {
        final byte[] arr = new byte[array.size()];
        int index = 0;
        for (final Byte value : array) {
            arr[index++] = value;
        }
        return arr;
    }
}