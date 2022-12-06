package eu.zickzenni.opencubes.util;

import java.util.HashMap;

public class SimpleProfiler {
    private static HashMap<String, Long> profiles = new HashMap<>();

    public static void start(String name) {
        profiles.put(name, System.currentTimeMillis());
    }

    public static void stop(String name) {
        System.out.println("[SimpleProfiler] " + name + " took: " + (System.currentTimeMillis() - profiles.get(name)) + "ms!");
        profiles.remove(name);
    }
}
