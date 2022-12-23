package eu.zickzenni.opencubes.client.mesh;

import eu.zickzenni.opencubes.block.Block;
import eu.zickzenni.opencubes.block.BlockSide;
import eu.zickzenni.opencubes.block.Blocks;
import eu.zickzenni.opencubes.client.texture.TextureManager;
import org.joml.Vector2f;

public final class CubeMesh {
    public static Face getFrontFace(int id) {
        Vector2f position = getUVPosition(id, BlockSide.FRONT);
        return new Face(new float[]{
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f
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
        return new Face(new float[]{
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f
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
        return new Face(new float[]{
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f
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
        return new Face(new float[]{
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f
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
        return new Face(new float[]{
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,

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
        return new Face(new float[]{
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,

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
        if (block.isUsingFullTexture()) {
            return TextureManager.getPosition(block.getFullTexture());
        }
        return switch (side) {
            case TOP -> TextureManager.getPosition(block.getTopTexture());
            case BOTTOM -> TextureManager.getPosition(block.getBottomTexture());
            case FRONT, BACK, LEFT, RIGHT -> TextureManager.getPosition(block.getSideTexture());
        };
    }
}