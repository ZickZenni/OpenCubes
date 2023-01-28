package net.opencubes.entity.player;

import net.opencubes.entity.Entity;
import net.opencubes.world.physics.Vec2;
import net.opencubes.world.physics.Vec3;

public class Player extends Entity {
    public Player(int id, Vec3 position, float yaw, float pitch) {
        super(id, position, yaw, pitch);
        eyePosition = new Vec3(0,2,0);
    }

    @Override
    public Vec2 getBoundingBox() {
        return new Vec2(0.5f, 2f);
    }
}