package eu.zickzenni.opencubes;

import eu.zickzenni.opencubes.client.GameSettings;
import eu.zickzenni.opencubes.client.GameWindow;
import eu.zickzenni.opencubes.client.engine.render.RenderSystem;
import eu.zickzenni.opencubes.client.engine.shader.DefaultShader;
import eu.zickzenni.opencubes.client.engine.shader.GuiShader;
import eu.zickzenni.opencubes.client.engine.shader.ShaderManager;
import eu.zickzenni.opencubes.client.engine.texture.TextureManager;
import eu.zickzenni.opencubes.client.gui.GuiScreen;
import eu.zickzenni.opencubes.client.gui.MainMenuScreen;
import eu.zickzenni.opencubes.world.World;
import org.lwjgl.opengl.GL11;

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

    private int targetUps = 100;

    private GameWindow window;
    private GuiScreen screen;

    private World world;

    private OpenCubes() throws Exception {
        instance = this;

        this.window = new GameWindow("OpenCubes", 856, 482, GameSettings.vSync);
        this.window.init();

        ShaderManager.registerShader("default", new DefaultShader("default.vert", "default.frag"));
        ShaderManager.registerShader("gui", new GuiShader("gui.vert", "gui.frag"));
        TextureManager.loadTextures();

        this.start();
        this.run();

        this.window.cleanup();
    }

    private void start() {
        window.setClearColor(0,0,0);
    }

    public void generateWorld() {
        if (world != null)
            return;
        world = World.createWorld("World");
        world.generate();
    }

    boolean running = true;

    private void run() {
        int targetFps = GameSettings.fps;

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
        } else {
            if (screen != null) {
                //screen.render();
            } else {
                MainMenuScreen.update(interval);
            }
        }
    }

    private void render() {
        window.update();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        if (world == null) {
            if (screen != null) {
                screen.render();
            } else {
                MainMenuScreen.render();
            }
        } else {
            RenderSystem.renderWorld();
            if (screen != null) {
                screen.render();
            }
        }
    }

    public static OpenCubes getInstance() {
        return instance;
    }

    public GameWindow getWindow() {
        return this.window;
    }

    public GuiScreen getScreen() {
        return screen;
    }

    public void setScreen(GuiScreen screen) {
        this.screen = screen;
    }

    public World getWorld() {
        return world;
    }
}