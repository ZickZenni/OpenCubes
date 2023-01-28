package net.opencubes.entity.player;

import net.opencubes.client.Camera;
import net.opencubes.client.OpenCubes;
import net.opencubes.client.platform.Window;
import net.opencubes.util.Ray;
import net.opencubes.world.level.Level;
import net.opencubes.world.level.chunk.ChunkBlock;
import net.opencubes.world.physics.Vec3;
import org.joml.Math;
import org.lwjgl.glfw.GLFW;

public class LocalPlayer extends Player {
    public GameMode gameMode;

    public LocalPlayer(int id, Vec3 position, float yaw, float pitch) {
        super(id, position, yaw, pitch);
        gameMode = GameMode.SURVIVAL;
    }

    public ChunkBlock selectedBlock;

    public void update(float dt) {
        Window window = OpenCubes.getInstance().getWindow();
        Camera mainCamera = OpenCubes.getInstance().gameRenderer.getMainCamera();
        Level level = OpenCubes.getInstance().getLevel();

        // Block Selection
        Ray ray = new Ray(new Vec3(0, 0.5f, 0).add(mainCamera.getPosition()), new Vec3(mainCamera.getYaw(), mainCamera.getPitch(), 0), 3.58f, 1000);
        for (int i = 0; i < ray.getSteps(); i++) {
            Vec3 rayPosition = ray.step();
            int x = floor(rayPosition.x);
            int y = floor(rayPosition.y);
            int z = floor(rayPosition.z);
            selectedBlock = level.getBlock(x, y, z);
            if (selectedBlock != null) {
                // Block Break


                if (window.getMouseInput().isLeftButtonPressed()) {
                    level.breakBlock(x, y, z);
                }

                /*
                // Item Interact
                if (window.getMouseInput().isRightButtonPressed() && screen == null) {
                    //PlayerEntity player = OpenCubes.getInstance().getPlayer();
                    //PlayerInventory inventory = player.getInventory();
                    //ItemStack itemStack = inventory.getItemStack(27 + inventory.getHotbarSlot());
                    if (itemStack != null) {
                        float rX = rayPosition.x - x - 0.5f;
                        float rY = rayPosition.y - y - 0.5f;
                        float rZ = rayPosition.z - z - 0.5f;
                        if (rZ > 0.495f && rZ > rY) {
                            itemStack.getItem().onBlockInteract(dimension, x, y, z, BlockSide.BACK);
                        } else if (rZ < -0.495 && rZ < rY) {
                            itemStack.getItem().onBlockInteract(dimension, x, y, z, BlockSide.FRONT);
                        } else if (rX > 0.495f && rX > rY) {
                            itemStack.getItem().onBlockInteract(dimension, x, y, z, BlockSide.RIGHT);
                        } else if (rX < -0.495f && rX < rY) {
                            itemStack.getItem().onBlockInteract(dimension, x, y, z, BlockSide.LEFT);
                        } else if (rY > 0.495f) {
                            itemStack.getItem().onBlockInteract(dimension, x, y, z, BlockSide.TOP);
                        } else if (rY < -0.495f) {
                            itemStack.getItem().onBlockInteract(dimension, x, y, z, BlockSide.BOTTOM);
                        }
                    }
                }*/

                //RenderSystem.renderSelectionBox(dimension, x, y, z);
                break;
            }
        }
    }

    @Override
    public void tick() {
        Camera mainCamera = OpenCubes.getInstance().gameRenderer.getMainCamera();
        Vec3 movement = new Vec3(0, 0, 0);

        if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_W)) {
            movement.z -= 1;
        }
        if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_S)) {
            movement.z += 1;
        }
        if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_A)) {
            movement.x -= 1;
        }
        if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_D)) {
            movement.x += 1;
        }
        if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_SPACE)) {
            movement.y += 1;
        }
        if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            movement.y -= 1;
        }
        float speed = 1 / 5f;
        if (OpenCubes.getInstance().getWindow().getKeyboardInput().isKeyHold(GLFW.GLFW_KEY_LEFT_CONTROL)) {
            speed = 1;
        }

        Vec3 force = new Vec3(0, 0, 0);
        force.x += Math.sin(Math.toRadians(mainCamera.getPitch())) * -1.0f * movement.z * speed;
        force.z += Math.cos(Math.toRadians(mainCamera.getPitch())) * movement.z * speed;
        force.x += Math.sin(Math.toRadians(mainCamera.getPitch() - 90)) * -1.0f * movement.x * speed;
        force.z += Math.cos(Math.toRadians(mainCamera.getPitch() - 90)) * movement.x * speed;
        force.y += movement.y * speed;

        if (force.x != 0 || force.y != 0 || force.z != 0) {
            movePosition(force.x, force.y, force.z);
        }
    }

    private int floor(float a) {
        return (int) java.lang.Math.floor(a);
    }
}