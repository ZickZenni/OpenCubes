package eu.zickzenni.opencubes.client.world;

import eu.zickzenni.opencubes.block.Block;
import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.GameWindow;
import eu.zickzenni.opencubes.client.OpenCubes;
import eu.zickzenni.opencubes.client.render.RenderSystem;
import eu.zickzenni.opencubes.client.util.GameSettings;
import eu.zickzenni.opencubes.util.Mathf;
import eu.zickzenni.opencubes.util.Ray;
import eu.zickzenni.opencubes.world.Dimension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
    private static final Logger logger = LogManager.getLogger("Camera");
    private final Vector3f position = new Vector3f();
    private final Vector3f rotation = new Vector3f();

    private boolean leftButtonPressed;
    private boolean rightButtonPressed;

    public void update(float interval) {
        GameWindow window = OpenCubes.getInstance().getWindow();
        Dimension dimension = OpenCubes.getInstance().getPlayer().getDimension();

        if (window.isFocused()) {
            Vector2f rotVec = window.getMouseInput().getDisplVec();
            moveRotation(rotVec.x * GameSettings.mouseSensitivity / 100f, rotVec.y * GameSettings.mouseSensitivity / 100f, 0);
            OpenCubes.getInstance().getPlayer().setRotation(rotation.x, rotation.y, rotation.z);
        }

        // Block Selection
        Ray ray = new Ray(new Vector3f(0, 0.5f, 0).add(position), rotation, 3.58f, 1000);
        for (int i = 0; i < ray.getSteps(); i++) {
            Vector3f position = ray.step();
            int x = floor(position.x);
            int y = floor(position.y);
            int z = floor(position.z);
            if (dimension.doesBlockExist(x, y, z)) {
                // Block Break
                if (window.getMouseInput().isLeftButtonPressed()) {
                    if (!leftButtonPressed) {
                        dimension.breakBlock(x, y, z);
                        leftButtonPressed =  true;
                    }
                } else {
                    leftButtonPressed = false;
                }

                // Block Place
                if (window.getMouseInput().isRightButtonPressed()) {
                    if (!rightButtonPressed) {
                        Block buildBlock = Blocks.STONE;
                        if (!buildBlock.equals(Blocks.AIR)) {
                            float rX = position.x - x - 0.5f;
                            float rY = position.y - y - 0.5f;
                            float rZ = position.z - z - 0.5f;

                            if (rZ > 0.495f && rZ > rY) {
                                dimension.placeBlock(x, y, z + 1, Blocks.STONE);
                            } else if (rZ < -0.495 && rZ < rY) {
                                dimension.placeBlock(x, y, z - 1, Blocks.STONE);
                            } else if (rX > 0.495f && rX > rY) {
                                dimension.placeBlock(x + 1, y, z, Blocks.STONE);
                            } else if (rX < -0.495f && rX < rY) {
                                dimension.placeBlock(x - 1, y, z, Blocks.STONE);
                            } else if (rY > 0.495f) {
                                dimension.placeBlock(x, y + 1, z, Blocks.STONE);
                            } else if (rY < -0.495f) {
                                dimension.placeBlock(x, y - 1, z, Blocks.STONE);
                            }

                            rightButtonPressed = true;
                        }
                    }
                } else {
                    rightButtonPressed = false;
                }

                RenderSystem.renderSelectionBox(dimension, x,y,z);
                break;
            }
        }

        Vector3f position = OpenCubes.getInstance().getPlayer().getPosition();
        this.position.x = Mathf.lerp(this.position.x, position.x, 15 * interval);
        this.position.y = Mathf.lerp(this.position.y, position.y + 2, 15 * interval);
        this.position.z = Mathf.lerp(this.position.z, position.z, 15 * interval);
    }

    public void tick() {
        GameWindow window = OpenCubes.getInstance().getWindow();
        Vector3f movement = new Vector3f(0, 0,0);
        if (window.getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_W)) {
            movement.z -= 1;
        }
        if (window.getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_S)) {
            movement.z += 1;
        }
        if (window.getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            movement.y -= 1;
        }
        if (window.getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_SPACE)) {
            movement.y += 1;
        }
        if (window.getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_A)) {
            movement.x -= 1;
        }
        if (window.getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_D)) {
            movement.x += 1;
        }
        float speed = 0.5f;
        OpenCubes.getInstance().getPlayer().movePosition(movement.x * speed, movement.y * speed, movement.z * speed);

    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x = Math.clamp(-89, 89, rotation.x + offsetX);
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    public Vector3f getPosition() {
        return new Vector3f(position.x, position.y, position.z);
    }

    public Vector3f getRotation() {
        Vector3f rotation = OpenCubes.getInstance().getPlayer().getRotation();
        return new Vector3f(this.rotation.x, rotation.y, rotation.z);
    }

    private int floor(float a) {
        return (int) java.lang.Math.floor(a);
    }
}
