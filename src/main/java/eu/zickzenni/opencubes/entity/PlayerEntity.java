package eu.zickzenni.opencubes.entity;

import eu.zickzenni.opencubes.player.GameMode;
import eu.zickzenni.opencubes.world.Dimension;

public class PlayerEntity extends LivingEntity {
    private GameMode gamemode = GameMode.CREATIVE;

    public PlayerEntity(int id, Dimension dimension) {
        super(id, dimension);
    }

    @Override
    public void update() {
        //super.update(interval);
    }

    public GameMode getGameMode() {
        return gamemode;
    }
}
