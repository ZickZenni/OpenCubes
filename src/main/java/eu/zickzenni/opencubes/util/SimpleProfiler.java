package eu.zickzenni.opencubes.util;

import java.util.HashMap;

public class SimpleProfiler {
    private static HashMap<String, Long> profiles = new HashMap<>();

    public static void start(String name) {
        profiles.put(name, System.currentTimeMillis());
    }

    public static long stop(String name) {
        long time = System.currentTimeMillis() - profiles.get(name);
        profiles.remove(name);
        return time;
    }
}
