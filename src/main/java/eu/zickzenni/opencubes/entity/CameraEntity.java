package eu.zickzenni.opencubes.entity;

import eu.zickzenni.opencubes.OpenCubes;
import eu.zickzenni.opencubes.client.GameWindow;
import eu.zickzenni.opencubes.world.Chunk;
import eu.zickzenni.opencubes.world.ChunkPosition;
import eu.zickzenni.opencubes.world.Dimension;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class CameraEntity extends Entity {
    public CameraEntity(int id, Dimension dimension) {
        super(id, dimension);
        setRotation(0,0,0);
    }

    @Override
    public Vector3f getSpawnLocation() {
        //return getDefaultSpawnLocation().add(0, 0, 0);
        return new Vector3f(0,100,0);
    }

    @Override
    public void update(float interval) {
        GameWindow window = OpenCubes.getInstance().getWindow();
        Vector3f movement = new Vector3f(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            movement.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            movement.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            movement.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            movement.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            movement.y -= 1;
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            movement.y += 1;
        }

        float speed = window.isKeyPressed(GLFW_KEY_LEFT_CONTROL) ? 14 * 2.7f : 14;
        // Update camera position
        movePosition(movement.x * speed * interval, movement.y * speed * interval, movement.z * speed * interval);

        // Update camera based on mouse
        if (window.getMouseInput().isRightButtonPressed()) {
            Vector2f rotVec = window.getMouseInput().getDisplVec();
            float x = rotVec.x * 10f * interval;
            if (getRotation().x + x > 89) {
                x = 0;
            } else if (getRotation().x + x < -89) {
                x = 0;
            }
            setRotation(getRotation().x + x, getRotation().y + rotVec.y * 10f * interval, 0);
        }
    }
}