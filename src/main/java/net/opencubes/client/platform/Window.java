package net.opencubes.client.platform;

import net.opencubes.client.platform.input.KeyboardInput;
import net.opencubes.client.platform.input.MouseInput;
import net.opencubes.client.systems.RenderSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Window {
    private static final Logger logger = LogManager.getLogger("GameWindow");

    private String title;
    private int width, height;
    private long handle;

    private boolean resize, vSync, fullScreen;

    private final MouseInput mouseInput;
    private final KeyboardInput keyboardInput;

    public Window(String title, boolean vSync, boolean fullScreen) {
        this.title = title;
        this.vSync = vSync;
        this.fullScreen = fullScreen;
        this.mouseInput = new MouseInput();
        this.keyboardInput = new KeyboardInput();
        this.init();
    }

    private boolean init() {
        GLFWErrorCallback.createPrint(System.err).set();

        logger.info("Initializing glfw...");

        if (!GLFW.glfwInit())
            return false;

        logger.info("Initialized glfw!");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
        //GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 8);

        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (vidMode == null)
            return false;

        this.width = vidMode.width();
        this.height = vidMode.height();

        handle = GLFW.glfwCreateWindow(width, height, title, fullScreen ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL, MemoryUtil.NULL);
        if (handle == MemoryUtil.NULL)
            return false;

        logger.info("Created window with size " + width + "x" + height);

        GLFW.glfwSetFramebufferSizeCallback(handle, (handle, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        GLFW.glfwMaximizeWindow(handle);

        GLFW.glfwMakeContextCurrent(handle);
        GLFW.glfwSwapInterval(vSync ? 1 : 0);

        GLFW.glfwShowWindow(handle);
        GL.createCapabilities();

        RenderSystem.clearColor(0,0,0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // RenderSystem.enableMSAA(); | [Bug] Lines visible on blocks

        mouseInput.init(this);
        keyboardInput.init(this);

        return true;
    }


    public void update() {
        GLFW.glfwSwapBuffers(handle);

        if (isFocused()) {
            GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, mouseInput.isCursorLocked() ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
        } else {
            GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }

    public void cleanup() {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
    }

    ///// Input

    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public KeyboardInput getKeyboardInput() {
        return keyboardInput;
    }

    /////

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        GLFW.glfwSetWindowTitle(handle, title);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isResize() {
        return resize;
    }

    public boolean isFocused() {
        return GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_FOCUSED) == 1;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public long getHandle() {
        return handle;
    }

    public boolean isVSync() {
        return vSync;
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }
}
