package net.opencubes.client.platform.input;

import net.opencubes.client.platform.Window;
import org.lwjgl.glfw.GLFW;

public class KeyboardInput {
    private static final int[] keys = new int[]{32, 39, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 59, 61, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 96, 161, 162, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 280, 281, 282, 283, 284, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 340, 341, 342, 343, 344, 345, 346, 347, 348};
    private static boolean hold[] = new boolean[349];
    private static boolean pressed[] = new boolean[349];
    private Window window;

    public void init(Window window) {
        this.window = window;
    }

    public void update() {
        for (int key : keys) {
            boolean state = isKey(key);
            if (state)  {
                if (!hold[key]) {
                    pressed[key] = true;
                } else {
                    pressed[key] = false;
                }
            } else {
                pressed[key] = false;
            }
            hold[key] = state;
        }
    }

    public boolean isKeyPressed(int key) {
        return pressed[key];
    }

    public boolean isKeyHold(int key) {
        return isKey(key);
    }

    public boolean isKey(int key) {
        return GLFW.glfwGetKey(window.getHandle(), key) == GLFW.GLFW_PRESS;
    }
}
