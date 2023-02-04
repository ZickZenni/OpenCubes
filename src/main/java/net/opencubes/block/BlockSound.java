package net.opencubes.block;

import net.opencubes.client.OpenCubes;
import net.opencubes.client.audio.SoundManager;
import org.joml.Random;

import java.util.Objects;

public class BlockSound {
    public static final BlockSound NONE = new BlockSound("", 0, 0);
    public static final BlockSound GRASS = new BlockSound("grass", 4, 6);
    public static final BlockSound GRAVEL = new BlockSound("gravel", 4, 4);
    public static final BlockSound STONE = new BlockSound("stone", 4, 6);
    public static final BlockSound SAND = new BlockSound("sand", 4, 5);
    public static final BlockSound SNOW = new BlockSound("snow", 4, 4);
    public static final BlockSound WOOD = new BlockSound("wood", 4, 6);

    private final String fileName;
    private final int digAmount;
    private final int stepAmount;

    private BlockSound(String fileName, int digAmount, int stepAmount) {
        this.fileName = fileName;
        this.digAmount = digAmount;
        this.stepAmount = stepAmount;

        SoundManager soundManager = OpenCubes.getInstance().soundManager;
        for (int i = 0; i < digAmount; i++) {
            soundManager.loadSound("dig." + fileName + (i + 1), "assets/sounds/dig/" + fileName + (i + 1) + ".ogg");
        }
        for (int i = 0; i < stepAmount; i++) {
            soundManager.loadSound("step." + fileName + (i + 1), "assets/sounds/step/" + fileName + (i + 1) + ".ogg");
        }
    }

    public int getDigAmount() {
        return digAmount;
    }

    public int getStepAmount() {
        return stepAmount;
    }

    public String getRandomDigSound() {
        if (digAmount <= 0)
            return "";
        Random random = new Random();
        return "dig." + fileName + (random.nextInt(digAmount) + 1);
    }

    public String getRandomStepSound() {
        if (stepAmount <= 0)
            return "";
        Random random = new Random();
        return "step." + fileName + (random.nextInt(digAmount) + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockSound that = (BlockSound) o;
        return Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }
}
