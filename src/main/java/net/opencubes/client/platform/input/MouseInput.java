package net.opencubes.client.platform.input;

import net.opencubes.client.platform.Window;
import net.opencubes.world.physics.Vec2;
import org.lwjgl.glfw.GLFW;

public class MouseInput {
    private final Vec2 previousPos;
    private final Vec2 currentPos;
    private final Vec2 displVec;
    private boolean inWindow = false;

    private boolean leftButtonFirst = false;
    private boolean leftButtonPressed = false;
    private boolean leftButtonHold = false;

    private boolean rightButtonFirst = false;
    private boolean rightButtonPressed = false;
    private boolean rightButtonHold = false;

    private boolean cursorLocked = false;

    private Window window;

    public MouseInput() {
        previousPos = new Vec2(-1, -1);
        currentPos = new Vec2(0, 0);
        displVec = new Vec2(0,0);
    }

    public void init(Window window) {
        this.window = window;
        GLFW.glfwSetCursorPosCallback(window.getHandle(), (windowHandle, xpos, ypos) -> {
            currentPos.x = (float) xpos;
            currentPos.y = (float) ypos;
        });
        GLFW.glfwSetCursorEnterCallback(window.getHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        GLFW.glfwSetMouseButtonCallback(window.getHandle(), (windowHandle, button, action, mode) -> {
            if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
                leftButtonHold = action == GLFW.GLFW_PRESS;
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
                rightButtonHold = action == GLFW.GLFW_PRESS;
            }
        });
    }

    public Vec2 getDisplVec() {
        return displVec;
    }

    public Vec2 getCurrentPos() {
        return currentPos;
    }

    public void update() {
        if (leftButtonHold) {
            if (!leftButtonPressed && leftButtonFirst) {
                leftButtonPressed = true;
                leftButtonFirst = false;
            } else {
                leftButtonPressed = false;
            }
        } else {
            leftButtonPressed = false;
            leftButtonFirst = true;
        }

        if (rightButtonHold) {
            if (!rightButtonPressed && rightButtonFirst) {
                rightButtonPressed = true;
                rightButtonFirst = false;
            } else {
                rightButtonPressed = false;
            }
        } else {
            rightButtonPressed = false;
            rightButtonFirst = true;
        }

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

    public void centerCursor() {
        setPosition(window.getWidth() / 2, window.getHeight() / 2);
        displVec.x = 0;
        displVec.y = 0;
        previousPos.x = window.getWidth() / 2f;
        previousPos.y = window.getHeight() / 2f;
    }

    public void setPosition(int x, int y) {
        GLFW.glfwSetCursorPos(window.getHandle(), x, y);
    }

    public boolean isLeftButtonHold() {
        return leftButtonHold;
    }

    public boolean isRightButtonHold() {
        return rightButtonHold;
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
