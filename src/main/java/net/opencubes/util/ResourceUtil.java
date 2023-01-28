package net.opencubes.util;

import net.opencubes.client.shader.Shader;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceUtil {
    public static String loadResource(String fileName) throws Exception {
        String result = null;
        try (InputStream in = Shader.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        } catch (NullPointerException ignored) {}
        return result;
    }
}
