package net.opencubes.client;

import com.beust.jcommander.internal.Nullable;
import net.opencubes.block.BlockRegistry;
import net.opencubes.client.audio.SoundManager;
import net.opencubes.client.block.model.BlockModelManager;
import net.opencubes.client.gui.components.Rect;
import net.opencubes.client.gui.components.font.Font;
import net.opencubes.client.gui.components.font.FontRenderer;
import net.opencubes.client.gui.screens.Screen;
import net.opencubes.client.level.postprocess.LevelAmbientOcclusion;
import net.opencubes.client.platform.Window;
import net.opencubes.client.renderer.GameRenderer;
import net.opencubes.client.renderer.texture.TextureAtlas;
import net.opencubes.client.shader.ShaderManager;
import net.opencubes.client.systems.RenderSystem;
import net.opencubes.entity.player.LocalPlayer;
import net.opencubes.network.User;
import net.opencubes.world.level.Level;
import net.opencubes.world.physics.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class OpenCubes {
    static OpenCubes instance;

    public static final String version = "Dev";

    public final GameRenderer gameRenderer;
    public final SoundManager soundManager;
    private final User user;

    private volatile boolean running;
    private final Window window;
    private int fps;
    private Screen screen;

    @Nullable
    public LocalPlayer player;
    @Nullable
    private Level level;

    private Font font;

    public final TextureAtlas atlas;

    public OpenCubes() {
        instance = this;

        this.user = new User("Dev", UUID.randomUUID().toString());
        this.window = new Window("OpenCubes", true, false);
        this.soundManager = new SoundManager();

        ShaderManager.init();
        BlockRegistry.init();
        BlockModelManager.loadModels();

        this.atlas = new TextureAtlas();

        this.font = new Font("/assets/textures/gui/font.png");
        FontRenderer.init(font);

        this.gameRenderer = new GameRenderer(this);

        loadWorld("world");

        this.running = true;
    }

    public void run() {
        long lastRun = System.currentTimeMillis();
        long lastTick = System.currentTimeMillis();
        long lastFrame = System.currentTimeMillis();
        long lastStatUpdate = System.currentTimeMillis();

        float deltaFps = 0;

        int fps = 0;
        int ticksInMilli = (int) ((1f / 20) * 1000);

        while (running) {
            if (this.window.shouldClose()) {
                this.stop();
                break;
            }
            window.pollEvents();

            // FPS Cap
            long currentTime = System.currentTimeMillis();
            deltaFps += (currentTime - lastRun) / (1000f / 144);
            lastRun = currentTime;

            // Tick update
            if (currentTime - lastTick >= ticksInMilli) {
                tick();
                lastTick = currentTime;
            }

            // Stats update (FPS)
            if (currentTime - lastStatUpdate >= 1000) {
                this.fps = fps;
                fps = 0;
                lastStatUpdate = currentTime;
            }

            // Render + Update
            if (deltaFps >= 1) {
                update((currentTime - lastFrame) / 1000f);
                fps++;
                lastFrame = currentTime;
                deltaFps = 0;
            }
        }
        shutdown();
    }

    private void update(float deltaTime) {
        window.getKeyboardInput().update();
        window.getMouseInput().update();

        if (level != null && window.isFocused()) {
            window.getMouseInput().setCursorLocked(screen == null);
        } else {
            window.getMouseInput().setCursorLocked(false);
        }

        if (window.getKeyboardInput().isKeyPressed(GLFW.GLFW_KEY_F6)) {
            LevelAmbientOcclusion.setEnabled(!LevelAmbientOcclusion.isEnabled());
            gameRenderer.getLevelRenderer().regenerateChunks();
        }

        if (window.isResize()) {
            RenderSystem.setViewport(window.getWidth(), window.getHeight());
            if (screen != null) {
                screen.init(this, window.getWidth(), window.getHeight());
            }
            window.setResize(false);
        }

        soundManager.update();
        gameRenderer.updateCamera(deltaTime);

        if (level != null && player != null) {
            player.update(deltaTime);
        }
        render(deltaTime);
    }

    private void render(float deltaTime) {
        window.update();
        RenderSystem.clearBuffers();
        Rect.reset();

        if (level != null && player != null)  {
            gameRenderer.getLevelRenderer().setLevel(level);
            gameRenderer.renderLevel();
        }
        if (screen != null) {
            screen.render((int) window.getMouseInput().getCurrentPos().x, (int) window.getMouseInput().getCurrentPos().y, deltaTime);
        }

        gameRenderer.resetVertexCount();
    }

    private void tick() {
        if (level != null) {
            level.tick();
        }
    }

    public void loadWorld(String name) {
        if (level != null) {
            return;
        }
        setScreen(null);
        this.level = new Level();
        this.player = (LocalPlayer) level.addEntity(LocalPlayer.class, new Vec3(0, 70, 0), 0, 0);
        this.gameRenderer.getMainCamera().setEntity(player);
        this.gameRenderer.getMainCamera().setDetached(false);
    }

    public void stop() {
        this.running = false;
    }

    public void shutdown() {
        this.running = false;
        window.cleanup();
    }

    public static OpenCubes getInstance() {
        return instance;
    }

    public Window getWindow() {
        return window;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
        if (this.screen != null) {
            this.screen.init(this, window.getWidth(), window.getHeight());
        }
    }

    public boolean isRunning() {
        return running;
    }

    public int getFps() {
        return fps;
    }

    public User getUser() {
        return user;
    }

    public Level getLevel() {
        return level;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}