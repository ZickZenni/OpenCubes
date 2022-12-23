package eu.zickzenni.opencubes.client;

import com.beust.jcommander.JCommander;
import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.gui.IngameGui;
import eu.zickzenni.opencubes.client.gui.MainMenuGui;
import eu.zickzenni.opencubes.client.gui.screen.Screen;
import eu.zickzenni.opencubes.client.session.Session;
import eu.zickzenni.opencubes.client.shader.DefaultShader;
import eu.zickzenni.opencubes.client.shader.GuiShader;
import eu.zickzenni.opencubes.client.shader.SelectionShader;
import eu.zickzenni.opencubes.client.shader.ShaderManager;
import eu.zickzenni.opencubes.client.sound.SoundManager;
import eu.zickzenni.opencubes.client.texture.AtlasManager;
import eu.zickzenni.opencubes.client.texture.TextureManager;
import eu.zickzenni.opencubes.client.util.AppArgs;
import eu.zickzenni.opencubes.client.util.GameSettings;
import eu.zickzenni.opencubes.client.world.Camera;
import eu.zickzenni.opencubes.client.world.LightingEngine;
import eu.zickzenni.opencubes.client.world.WorldRenderer;
import eu.zickzenni.opencubes.entity.PlayerEntity;
import eu.zickzenni.opencubes.world.World;
import eu.zickzenni.opencubes.world.chunk.ChunkMeshSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.UUID;

public class OpenCubes {
    private static final Logger logger = LogManager.getLogger("OpenCubes");

    public static void main(String[] args) {
        try {
            AppArgs appArgs = new AppArgs();
            JCommander.newBuilder()
                    .addObject(appArgs)
                    .build()
                    .parse(args);
            new OpenCubes(appArgs);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static final String VERSION = "Dev";
    public static final int targetTps = 20;

    private static OpenCubes instance;

    private final Session session;

    private int fps;
    private int tps;

    public boolean screenshotMode;
    public boolean debugMenu;

    private GameWindow window;
    private SoundManager soundManager;

    private Screen screen;

    private MainMenuGui mainMenuGui;
    private IngameGui ingameGui;

    private Camera camera;
    private PlayerEntity player;
    private World world;

    private OpenCubes(AppArgs args) throws Exception {
        instance = this;

        if (args.getUsername() == null) {
            throw new RuntimeException("Username must be defined!");
        }
        if (args.getUsername().length() == 0 || args.getUsername().length() > 16) {
            throw new RuntimeException("Username cannot be smaller than 1 or larger than 16 characters!");
        }

        this.session = new Session(args.getUsername(), UUID.randomUUID());

        logger.info("OpenCubes Version: " + VERSION);

        String[] lwjglVersion = GLFW.glfwGetVersionString().split(" ");
        //                              Version                 OS
        logger.info("LWJGL Version: " + lwjglVersion[0] + " " + lwjglVersion[1]);
        logger.info("Session started with username: " + session.getUsername());
        logger.info("Player UUID: " + session.getUUID().toString());

        this.window = new GameWindow("OpenCubes " + VERSION, 856, 482, GameSettings.vSync);
        this.window.init();
        this.soundManager = new SoundManager();
        this.soundManager.init();

        this.start();
        this.run();

        this.soundManager.cleanup();
        this.window.cleanup();
    }

    private void start() throws Exception {
        window.setClearColor(0,0,0);

        ShaderManager.registerShader("selection", new SelectionShader("selection", "selection.vert", "selection.frag"));
        ShaderManager.registerShader("default", new DefaultShader("default", "default.vert", "default.frag"));
        ShaderManager.registerShader("gui", new GuiShader("gui.vert", "gui.frag"));

        TextureManager.loadTextures();
        AtlasManager.init();
        //BlockModelManager.loadModels();
        Blocks.init();

        this.mainMenuGui = new MainMenuGui();
        this.ingameGui = new IngameGui();
    }

    public void generateWorld() {
        if (world != null)
            return;
        this.window.getMouseInput().setCursorLocked(true);
        this.world = World.createWorld("World");
        this.world.generate();

        this.player = (PlayerEntity) world.addEntity(new PlayerEntity(0, world.getOverworld()));
        this.camera = new Camera();
    }

    boolean running = true;

    private void run() {
        long lastRun = System.currentTimeMillis();
        long lastTick = System.currentTimeMillis();
        long lastFrame = System.currentTimeMillis();
        long lastStatUpdate = System.currentTimeMillis();

        float deltaFps = 0;

        int fps = 0;
        int tps = 0;
        int ticksInMilli = (int) ((1f / targetTps) * 1000);

        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            // FPS Cap
            long currentTime = System.currentTimeMillis();
            deltaFps += (currentTime - lastRun) / (1000f / GameSettings.fps);
            lastRun = currentTime;

            // Tick update
            if (currentTime - lastTick >= ticksInMilli) {
                tick();
                tps++;
                lastTick = currentTime;
                ChunkMeshSystem.update();
            }

            // Stats update (FPS, TPS)
            if (currentTime - lastStatUpdate >= 1000) {
                this.fps = fps;
                this.tps = tps;
                fps = 0;
                tps = 0;
                lastStatUpdate = currentTime;
            }

            // Render + Update
            if (deltaFps >= 1) {
                input();
                update((currentTime - lastFrame) / 1000f);
                fps++;
                lastFrame = currentTime;
                deltaFps = 0;
            }
        }
    }

    private void input() {
        window.getMouseInput().input();
    }

    private void update(float deltaTime) {
        LightingEngine.update();
        soundManager.update();

        if (world != null) {
            if (camera != null) {
                camera.update(deltaTime);
                world.update(deltaTime);
                ingameGui.update(deltaTime);
            }
        } else {
            if (screen != null) {
                screen.render();
            } else {
                mainMenuGui.update(deltaTime);
            }
        }
        render();
    }

    private void tick() {
        if (world != null) {
            world.tick();
        }
        if (camera != null) {
            camera.tick();
        }
    }

    private void render() {
        window.update();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        if (world == null) {
            if (screen != null) {
                screen.render();
            } else {
                mainMenuGui.render(window.getWidth(),window.getHeight());
            }
        } else {
            if (camera != null) {
                WorldRenderer.renderWorld();
                ingameGui.render(window.getWidth(), window.getHeight());
                if (screen != null) {
                    screen.render();
                }
            }
        }
    }

    public static OpenCubes getInstance() {
        return instance;
    }

    public GameWindow getWindow() {
        return this.window;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public World getWorld() {
        return world;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public Camera getCamera() {
        return camera;
    }

    public int getFps() {
        return fps;
    }

    public int getTps() {
        return tps;
    }

    public Session getSession() {
        return session;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}