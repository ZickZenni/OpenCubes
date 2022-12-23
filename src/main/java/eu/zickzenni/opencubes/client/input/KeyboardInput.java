package eu.zickzenni.opencubes.client.input;

import eu.zickzenni.opencubes.client.GameWindow;
import org.lwjgl.glfw.GLFW;

public class KeyboardInput {
    private static boolean pressed[] = new boolean[65535];
    private GameWindow window;

    public void init(GameWindow window) {
        this.window = window;
    }

    public boolean isKeyPressed(int key) {
        boolean state = isKey(key);
        if (!pressed[key] && state) {
            pressed[key] = state;
            return true;
        }
        pressed[key] = state;
        return false;
    }

    public boolean isKeyHold(int key) {
        return isKey(key);
    }

    private boolean isKey(int key) {
        return GLFW.glfwGetKey(window.getHandle(), key) == GLFW.GLFW_PRESS;
    }
}
