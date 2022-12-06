package eu.zickzenni.opencubes.client.mesh;

import eu.zickzenni.opencubes.block.Block;
import eu.zickzenni.opencubes.block.BlockSide;
import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.engine.mesh.Face;
import eu.zickzenni.opencubes.client.engine.mesh.Mesh;
import eu.zickzenni.opencubes.client.engine.texture.Texture;
import eu.zickzenni.opencubes.client.engine.texture.TextureManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CubeMesh extends Mesh {
    static final float[] positions = new float[]{
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,
            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,
            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,
            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,};

    static final int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            6, 15, 14, 6, 14, 4,
            // Bottom face
            19, 18, 16, 19, 16, 17,
            // Back face
            7, 4, 5, 7, 6, 4};

    public CubeMesh(float[] textCoords, Texture texture) {
        super(new Face[]{
                new Face(new Vector3f[]{new Vector3f(-0.5f, 0.5f, 0.5f)}, textCoords,new float[]{
                        1,1,1,
                        1,1,1,
                        1,1,1,
                        1,1,1,
                },  new int[]{0, 1, 3, 3, 1, 2,})}, texture);
    }

    public static Face getFrontFace(int id) {
        Vector2f position = getUVPosition(id, BlockSide.FRONT);
        return new Face(new Vector3f[]{
                new Vector3f(-0.5f, 0.5f, 0.5f),
                new Vector3f(-0.5f, -0.5f, 0.5f),
                new Vector3f(0.5f, -0.5f, 0.5f),
                new Vector3f(0.5f, 0.5f, 0.5f)
        }, new float[]{
                position.x, position.y,
                position.x, position.y + TextureManager.TEXTURE_SIZE,
                position.x + TextureManager.TEXTURE_SIZE, position.y + TextureManager.TEXTURE_SIZE,
                position.x + TextureManager.TEXTURE_SIZE, position.y,
        }, new float[]{
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
        }, new int[]{0, 1, 3, 3, 1, 2,});
    }

    public static Face getBackFace(int id) {
        Vector2f position = getUVPosition(id, BlockSide.BACK);
        return new Face(new Vector3f[]{
                new Vector3f(-0.5f, 0.5f, -0.5f),
                new Vector3f(0.5f, 0.5f, -0.5f),
                new Vector3f(-0.5f, -0.5f, -0.5f),
                new Vector3f(0.5f, -0.5f, -0.5f)
        }, new float[]{
                position.x, position.y,
                position.x + TextureManager.TEXTURE_SIZE, position.y,
                position.x, position.y + TextureManager.TEXTURE_SIZE,
                position.x + TextureManager.TEXTURE_SIZE, position.y + TextureManager.TEXTURE_SIZE,
        }, new float[]{
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
        }, new int[]{3, 0, 1, 3, 2, 0});
    }

    public static Face getTopFace(int id) {
        Vector2f position = getUVPosition(id, BlockSide.TOP);
        return new Face(new Vector3f[]{
                new Vector3f(-0.5f, 0.5f, -0.5f),
                new Vector3f(0.5f, 0.5f, -0.5f),
                new Vector3f(-0.5f, 0.5f, 0.5f),
                new Vector3f(0.5f, 0.5f, 0.5f)
        }, new float[]{
                position.x, position.y,
                position.x + TextureManager.TEXTURE_SIZE, position.y,
                position.x, position.y + TextureManager.TEXTURE_SIZE,
                position.x + TextureManager.TEXTURE_SIZE, position.y + TextureManager.TEXTURE_SIZE,
        }, new float[]{
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
        }, new int[]{0, 2, 3, 1, 0, 3});
    }

    public static Face getBottomFace(int id) {
        Vector2f position = getUVPosition(id, BlockSide.BOTTOM);
        return new Face(new Vector3f[]{
                new Vector3f(-0.5f, -0.5f, -0.5f),
                new Vector3f(0.5f, -0.5f, -0.5f),
                new Vector3f(-0.5f, -0.5f, 0.5f),
                new Vector3f(0.5f, -0.5f, 0.5f)
        }, new float[]{
                position.x, position.y,
                position.x + TextureManager.TEXTURE_SIZE, position.y,
                position.x, position.y + TextureManager.TEXTURE_SIZE,
                position.x + TextureManager.TEXTURE_SIZE, position.y + TextureManager.TEXTURE_SIZE,
        }, new float[]{
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
        }, new int[]{3, 2, 0, 3, 0, 1});
    }

    public static Face getLeftFace(int id) {
        Vector2f position = getUVPosition(id, BlockSide.LEFT);
        return new Face(new Vector3f[]{
                new Vector3f(-0.5f, 0.5f, -0.5f),
                new Vector3f(-0.5f, -0.5f, -0.5f),
                new Vector3f(-0.5f, -0.5f, 0.5f),
                new Vector3f(-0.5f, 0.5f, 0.5f),

        }, new float[]{
                position.x, position.y,
                position.x, position.y + TextureManager.TEXTURE_SIZE,
                position.x + TextureManager.TEXTURE_SIZE, position.y + TextureManager.TEXTURE_SIZE,
                position.x + TextureManager.TEXTURE_SIZE, position.y,
        }, new float[]{
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
        }, new int[]{0, 1, 3, 3, 1, 2});
    }

    public static Face getRightFace(int id) {
        Vector2f position = getUVPosition(id, BlockSide.RIGHT);
        return new Face(new Vector3f[]{
                new Vector3f(0.5f, 0.5f, 0.5f),
                new Vector3f(0.5f, -0.5f, 0.5f),
                new Vector3f(0.5f, -0.5f, -0.5f),
                new Vector3f(0.5f, 0.5f, -0.5f),

        }, new float[]{
                position.x, position.y,
                position.x, position.y + TextureManager.TEXTURE_SIZE,
                position.x + TextureManager.TEXTURE_SIZE, position.y + TextureManager.TEXTURE_SIZE,
                position.x + TextureManager.TEXTURE_SIZE, position.y,
        }, new float[]{
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
        }, new int[]{0, 1, 3, 3, 1, 2}
        );
    }

    private static Vector2f getUVPosition(int id, BlockSide side) {
        Block block = Blocks.getBlock(id);
        if (block == null) {
            return new Vector2f(-TextureManager.TEXTURE_SIZE, -TextureManager.TEXTURE_SIZE);
        }
        if (block.getTexture().isSingleTexture()) {
            return TextureManager.getPosition(block.getTexture().getSingleAtlasPosition());
        }
        if (!block.getTexture().getAtlasPositions().containsKey(side)) {
            return new Vector2f(-TextureManager.TEXTURE_SIZE, -TextureManager.TEXTURE_SIZE);
        }
        return TextureManager.getPosition(block.getTexture().getAtlasPositions().get(side));
    }
}