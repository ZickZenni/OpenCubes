package eu.zickzenni.opencubes.client.input;

import eu.zickzenni.opencubes.client.GameWindow;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseInput {
    private final Vector2d previousPos;
    private final Vector2d currentPos;
    private final Vector2f displVec;
    private boolean inWindow = false;

    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;
    private boolean cursorLocked = false;

    private GameWindow window;

    public MouseInput() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(GameWindow window) {
        this.window = window;
        GLFW.glfwSetCursorPosCallback(window.getHandle(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });
        GLFW.glfwSetCursorEnterCallback(window.getHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        GLFW.glfwSetMouseButtonCallback(window.getHandle(), (windowHandle, button, action, mode) -> {
            if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
                leftButtonPressed = action == GLFW.GLFW_PRESS;
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
                rightButtonPressed = action == GLFW.GLFW_PRESS;
            }
        });
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public void input() {
        displVec.x = 0;
        displVec.y = 0;
        if (inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public void setPosition(int x, int y) {
        GLFW.glfwSetCursorPos(window.getHandle(), x, y);
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public boolean isCursorLocked() {
        return cursorLocked;
    }

    public void setCursorLocked(boolean cursorLocked) {
        this.cursorLocked = cursorLocked;
    }
}
