package net.opencubes.client.vertex.util;

import net.opencubes.block.Block;
import net.opencubes.block.BlockRegistry;
import net.opencubes.block.BlockSide;
import net.opencubes.client.OpenCubes;
import net.opencubes.client.block.model.BlockModel;
import net.opencubes.client.block.model.BlockModelManager;
import net.opencubes.client.block.model.BlockModelTexture;
import net.opencubes.client.level.chunk.ChunkMesh;
import net.opencubes.client.level.postprocess.LevelAmbientOcclusion;
import net.opencubes.client.renderer.texture.AtlasPosition;
import net.opencubes.client.vertex.Face;
import net.opencubes.client.vertex.Mesh;
import net.opencubes.world.physics.Vec3;
import net.opencubes.world.physics.Vec4;

import java.util.HashMap;

public final class CubeMesh {
    public static Face getFrontFace(String name) {
        return getFrontFace(name, 1, 1, 1);
    }

    public static Face getFrontFace(String name, float sizeX, float sizeY, float sizeZ) {
        Vec4 position = getUVPosition(name, BlockSide.FRONT);
        return new Face(new float[]{
                -(sizeX / 2), (sizeY / 2), (sizeZ / 2),
                -(sizeX / 2), -(sizeY / 2), (sizeZ / 2),
                (sizeX / 2), -(sizeY / 2), (sizeZ / 2),
                (sizeX / 2), (sizeY / 2), (sizeZ / 2)
        }, new float[]{
                position.x, position.y,
                position.x, position.y + position.w,
                position.x + position.z, position.y + position.w,
                position.x + position.z, position.y,
        }, new float[]{
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
        }, new int[]{0, 1, 3, 3, 1, 2,});
    }

    public static Face getBackFace(String name) {
        return getBackFace(name, 1, 1, 1);
    }

    public static Face getBackFace(String name, float sizeX, float sizeY, float sizeZ) {
        Vec4 position = getUVPosition(name, BlockSide.BACK);
        return new Face(new float[]{
                -(sizeX / 2), (sizeY / 2), -(sizeZ / 2),
                (sizeX / 2), (sizeY / 2), -(sizeZ / 2),
                -(sizeX / 2), -(sizeY / 2), -(sizeZ / 2),
                (sizeX / 2), -(sizeY / 2), -(sizeZ / 2)
        }, new float[]{
                position.x, position.y,
                position.x + position.z, position.y,
                position.x, position.y + position.w,
                position.x + position.z, position.y + position.w,
        }, new float[]{
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
        }, new int[]{3, 0, 1, 3, 2, 0});
    }

    public static Face getTopFace(String name) {
        return getTopFace(name, 1, 1, 1);
    }

    public static Face getTopFace(String name, float sizeX, float sizeY, float sizeZ) {
        Vec4 position = getUVPosition(name, BlockSide.TOP);
        return new Face(new float[]{
                -(sizeX / 2), (sizeY / 2), -(sizeZ / 2),
                (sizeX / 2), (sizeY / 2), -(sizeZ / 2),
                -(sizeX / 2), (sizeY / 2), (sizeZ / 2),
                (sizeX / 2), (sizeY / 2), (sizeZ / 2)
        }, new float[]{
                position.x, position.y,
                position.x + position.z, position.y,
                position.x, position.y + position.w,
                position.x + position.z, position.y + position.w,
        }, new float[]{
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
        }, new int[]{0, 2, 3, 1, 0, 3});
    }

    public static Face getBottomFace(String name) {
        return getBottomFace(name, 1, 1, 1);
    }

    public static Face getBottomFace(String name, float sizeX, float sizeY, float sizeZ) {
        Vec4 position = getUVPosition(name, BlockSide.BOTTOM);
        return new Face(new float[]{
                -(sizeX / 2), -(sizeY / 2), -(sizeZ / 2),
                (sizeX / 2), -(sizeY / 2), -(sizeZ / 2),
                -(sizeX / 2), -(sizeY / 2), (sizeZ / 2),
                (sizeX / 2), -(sizeY / 2), (sizeZ / 2)
        }, new float[]{
                position.x, position.y,
                position.x + position.z, position.y,
                position.x, position.y + position.w,
                position.x + position.z, position.y + position.w,
        }, new float[]{
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
        }, new int[]{3, 2, 0, 3, 0, 1});
    }

    public static Face getLeftFace(String name) {
        return getLeftFace(name, 1, 1, 1);
    }

    public static Face getLeftFace(String name, float sizeX, float sizeY, float sizeZ) {
        Vec4 position = getUVPosition(name, BlockSide.LEFT);
        return new Face(new float[]{
                -(sizeX / 2), (sizeY / 2), -(sizeZ / 2),
                -(sizeX / 2), -(sizeY / 2), -(sizeZ / 2),
                -(sizeX / 2), -(sizeY / 2), (sizeZ / 2),
                -(sizeX / 2), (sizeY / 2), (sizeZ / 2),

        }, new float[]{
                position.x, position.y,
                position.x, position.y + position.w,
                position.x + position.z, position.y + position.w,
                position.x + position.z, position.y,
        }, new float[]{
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
        }, new int[]{0, 1, 3, 3, 1, 2});
    }

    public static Face getRightFace(String name) {
        return getRightFace(name, 1, 1, 1);
    }

    public static Face getRightFace(String name, float sizeX, float sizeY, float sizeZ) {
        Vec4 position = getUVPosition(name, BlockSide.RIGHT);
        return new Face(new float[]{
                (sizeX / 2), (sizeY / 2), (sizeZ / 2),
                (sizeX / 2), -(sizeY / 2), (sizeZ / 2),
                (sizeX / 2), -(sizeY / 2), -(sizeZ / 2),
                (sizeX / 2), (sizeY / 2), -(sizeZ / 2),

        }, new float[]{
                position.x, position.y,
                position.x, position.y + position.w,
                position.x + position.z, position.y + position.w,
                position.x + position.z, position.y,
        }, new float[]{
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1,
        }, new int[]{0, 1, 3, 3, 1, 2}
        );
    }

    public static Vec4 getUVPosition(String name, BlockSide side) {
        if (name == null) {
            return getUVFromAtlas(null);
        }
        Block block = BlockRegistry.getBlock(name);
        if (block == null) {
            return getUVFromAtlas("");
        }
        BlockModel model = BlockModelManager.getModel(name);
        if (model == null) {
            return getUVFromAtlas("");
        }
        HashMap<String, BlockModelTexture> textures = model.getTextures();
        if (textures.containsKey(side.toString().toLowerCase())) {
            return getUVFromAtlas(textures.get(side.toString().toLowerCase()).texture());
        }
        if (textures.containsKey("all")) {
            return getUVFromAtlas(textures.get("all").texture());
        }
        return getUVFromAtlas("");
    }


    private static Vec4 getUVFromAtlas(String name) {
        int atlasSize = OpenCubes.getInstance().atlas.getTexture().getWidth();
        float pixelSize = 1f / atlasSize;
        if (name == null) {
            return new Vec4((atlasSize - 3) * pixelSize, (atlasSize - 1) * pixelSize, 1 * pixelSize, 1 * pixelSize);
        }
        if (name.length() == 0) {
            return new Vec4((atlasSize - 2) * pixelSize, (atlasSize - 2) * pixelSize, 2 * pixelSize, 2 * pixelSize);
        }
        AtlasPosition position = OpenCubes.getInstance().atlas.getPosition(name);
        if (position == null) {
            return getUVFromAtlas("");
        }

        float x = position.x() * pixelSize;
        float y = position.y() * pixelSize;
        float width = position.width() * pixelSize;
        float height = position.height() * pixelSize;

        return new Vec4(x, y, width, height);
    }

    public static Mesh createCubeMesh(String name, float r, float g, float b, float a) {
        return createCubeMesh(name, r, g, b, a, false);
    }

    public static Mesh createCubeMesh(String name, float r, float g, float b, float a, boolean tintFaces) {
        return createCubeMesh(name, r, g, b, a, 1, 1, 1, tintFaces);
    }

    public static Mesh createCubeMesh(String name, float r, float g, float b, float a, float sizeX, float sizeY, float sizeZ, boolean tintFaces) {
        MeshBuilder builder = new MeshBuilder();
        if (!tintFaces) {
            float[] colors = {
                    r, g, b, a,
                    r, g, b, a,
                    r, g, b, a,
                    r, g, b, a,
            };
            builder.addFace(CubeMesh.getTopFace(name, sizeX, sizeY, sizeZ).setColors(colors));
            builder.addFace(CubeMesh.getBottomFace(name, sizeX, sizeY, sizeZ).setColors(colors));
            builder.addFace(CubeMesh.getLeftFace(name, sizeX, sizeY, sizeZ).setColors(colors));
            builder.addFace(CubeMesh.getRightFace(name, sizeX, sizeY, sizeZ).setColors(colors));
            builder.addFace(CubeMesh.getFrontFace(name, sizeX, sizeY, sizeZ).setColors(colors));
            builder.addFace(CubeMesh.getBackFace(name, sizeX, sizeY, sizeZ).setColors(colors));
        } else {
            Block block = BlockRegistry.getBlock(name);

            Vec3 topTint = block.getTint(BlockSide.TOP);
            Vec3 bottomTint = block.getTint(BlockSide.BOTTOM);
            Vec3 leftTint = block.getTint(BlockSide.LEFT);
            Vec3 rightTint = block.getTint(BlockSide.RIGHT);
            Vec3 frontTint = block.getTint(BlockSide.FRONT);
            Vec3 backTint = block.getTint(BlockSide.BACK);

            builder.addFace(CubeMesh.getTopFace(name, sizeX, sizeY, sizeZ).setColors(ChunkMesh.computeColors(1, new Vec3(r * topTint.x, g * topTint.y, b * topTint.z), LevelAmbientOcclusion.getDefaultAmbientOcclusion())));
            builder.addFace(CubeMesh.getBottomFace(name, sizeX, sizeY, sizeZ).setColors(ChunkMesh.computeColors(1, new Vec3(r * bottomTint.x, g * bottomTint.y, b * bottomTint.z), LevelAmbientOcclusion.getDefaultAmbientOcclusion())));
            builder.addFace(CubeMesh.getLeftFace(name, sizeX, sizeY, sizeZ).setColors(ChunkMesh.computeColors(1, new Vec3(r * leftTint.x, g * leftTint.y, b * leftTint.z), LevelAmbientOcclusion.getDefaultAmbientOcclusion())));
            builder.addFace(CubeMesh.getRightFace(name, sizeX, sizeY, sizeZ).setColors(ChunkMesh.computeColors(1, new Vec3(r * rightTint.x, g * rightTint.y, b * rightTint.z), LevelAmbientOcclusion.getDefaultAmbientOcclusion())));
            builder.addFace(CubeMesh.getFrontFace(name, sizeX, sizeY, sizeZ).setColors(ChunkMesh.computeColors(1, new Vec3(r * frontTint.x, g * frontTint.y, b * frontTint.z), LevelAmbientOcclusion.getDefaultAmbientOcclusion())));
            builder.addFace(CubeMesh.getBackFace(name, sizeX, sizeY, sizeZ).setColors(ChunkMesh.computeColors(1, new Vec3(r * backTint.x, g * backTint.y, b * backTint.z), LevelAmbientOcclusion.getDefaultAmbientOcclusion())));
        }
        return builder.build();
    }
}