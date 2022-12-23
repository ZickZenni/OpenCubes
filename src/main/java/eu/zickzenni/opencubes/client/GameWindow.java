package eu.zickzenni.opencubes.client;

import eu.zickzenni.opencubes.client.input.KeyboardInput;
import eu.zickzenni.opencubes.client.input.MouseInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
public class GameWindow {
    private static final Logger logger = LogManager.getLogger("GameWindow");

    public static final float FOV = (float) Math.toRadians(80);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000;

    private String title;
    private int width, height;
    private long handle;

    private boolean resize, vSync;

    private MouseInput mouseInput;
    private KeyboardInput keyboardInput;

    private final Matrix4f projection;

    public GameWindow(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.projection = new Matrix4f();
        this.mouseInput = new MouseInput();
        this.keyboardInput = new KeyboardInput();
    }

    public boolean init() {
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

        handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (handle == MemoryUtil.NULL)
            return false;

        logger.info("Created window with size " + width + "x" + height);

        GLFW.glfwSetFramebufferSizeCallback(handle, (handle, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(handle, (handle, key, scancode, action, mods) -> {
           if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
               GLFW.glfwSetWindowShouldClose(handle, true);
           }
        });

        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(handle, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        GLFW.glfwMakeContextCurrent(handle);
        GLFW.glfwSwapInterval(vSync ? 1 : 0);

        GLFW.glfwShowWindow(handle);
        GL.createCapabilities();

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

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

        if (resize) {
            GL11.glViewport(0, 0, width, height);
            resize = false;
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

    public void setClearColor(float r, float g, float b) {
        GL11.glClearColor(r, g, b, 1);
    }

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

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f updateProjection()  {
        return updateProjection(projection, width, height);
    }

    public Matrix4f updateProjection(Matrix4f matrix, int width, int height)  {
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }
}
