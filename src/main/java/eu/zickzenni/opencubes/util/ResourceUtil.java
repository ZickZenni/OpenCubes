package eu.zickzenni.opencubes.util;

import java.io.File;
import java.net.URL;

public class ResourceUtil {
    public static File[] getResourceFolderFiles (String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        if (url == null) {
            return new File[0];
        }
        String path = url.getPath();
        return new File(path).listFiles();
    }
}
