package eu.zickzenni.opencubes.client.engine.render;

import eu.zickzenni.opencubes.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    private static final Matrix4f viewMatrix = new Matrix4f();
    private static final Matrix4f modelViewMatrix = new Matrix4f();

    public static Matrix4f getViewMatrix() {
        Vector3f cameraPos = World.getCamera().getPosition();
        Vector3f rotation = World.getCamera().getRotation();

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public static Matrix4f getModelViewMatrix(Model model, Matrix4f viewMatrix) {
        Vector3f rotation = model.getRotation();
        modelViewMatrix.identity().translate(model.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(model.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }
}