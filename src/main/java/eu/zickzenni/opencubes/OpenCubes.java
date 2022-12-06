package eu.zickzenni.opencubes;

import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.GameWindow;
import eu.zickzenni.opencubes.client.engine.mesh.Mesh;
import eu.zickzenni.opencubes.client.engine.render.Model;
import eu.zickzenni.opencubes.client.engine.render.RenderSystem;
import eu.zickzenni.opencubes.client.engine.render.Transform;
import eu.zickzenni.opencubes.client.engine.shader.DefaultShader;
import eu.zickzenni.opencubes.client.engine.shader.GuiShader;
import eu.zickzenni.opencubes.client.engine.shader.ShaderManager;
import eu.zickzenni.opencubes.client.engine.shader.ShaderProgram;
import eu.zickzenni.opencubes.client.engine.texture.Texture;
import eu.zickzenni.opencubes.client.engine.texture.TextureManager;
import eu.zickzenni.opencubes.client.engine.util.Timer;
import eu.zickzenni.opencubes.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class OpenCubes {
    public static void main(String[] args) {
        try {
            new OpenCubes();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static OpenCubes instance;

    private int targetFps = 144;
    private int targetUps = 100;

    private GameWindow window;
    private World world;

    private OpenCubes() throws Exception {
        instance = this;

        this.window = new GameWindow("OpenCubes", 856, 482, false);
        this.window.init();

        ShaderManager.registerShader("default", new DefaultShader("default.vert", "default.frag"));
        ShaderManager.registerShader("gui", new GuiShader("gui.vert", "gui.frag"));
        TextureManager.loadTextures();

        this.start();
        this.run();

        this.window.cleanup();
    }

    private void start() {
        world = World.createWorld("World");
        world.generate();
    }

    /*private void run() {
        float elapsedTime;
        float accumulator = 0f;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= frameTime) {
                update(accumulator - frameTime);
                accumulator -= frameTime;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }*/

    boolean running = true;

    private void run() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                input();
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                update(diffTimeMillis / 1000f);
                updateTime = now;
                deltaUpdate--;
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                render();
                deltaFps--;
            }
            initialTime = now;
        }
    }

    private void input() {
        window.getMouseInput().input();
    }

    private void update(float interval) {
        if (world != null) {
            if (World.getCamera() != null) {
                world.update(interval);
            }
        }
    }

    private void render() {
        window.update();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0.52f, 0.8f, 0.92f, 0.0f);
        RenderSystem.renderWorld();
    }

    public static OpenCubes getInstance() {
        return instance;
    }

    public GameWindow getWindow() {
        return this.window;
    }

    public World getWorld() {
        return world;
    }
}