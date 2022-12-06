package eu.zickzenni.opencubes.client.engine.mesh;

import java.util.HashMap;

public final class MeshManager {
    private MeshManager() {}

    private static HashMap<String, Mesh> meshes = new HashMap<>();

    public static void registerMesh(String name, Mesh mesh) {
        if (!meshes.containsKey(name)) {
            meshes.put(name, mesh);
        }
    }

    public static Mesh getMesh(String name) {
        return meshes.getOrDefault(name, null);
    }
}