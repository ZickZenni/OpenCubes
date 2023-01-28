package net.opencubes.client.audio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Logger logger = LogManager.getLogger("SoundManager");

    private long device;
    private long context;

    private final Map<String, Sound> soundBufferList;
    private final ArrayList<SoundSource> soundSourceList;

    public SoundManager() {
        soundBufferList = new HashMap<>();
        soundSourceList = new ArrayList<>();

        logger.info("Initializing sound manager...");

        this.device = ALC10.alcOpenDevice((ByteBuffer) null);
        if (device == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        this.context = ALC10.alcCreateContext(device, (IntBuffer) null);
        if (context == MemoryUtil.NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
        logger.info("Initialized sound manager!");
    }

    public void update() {
        for (int i = 0; i < soundSourceList.size(); i++) {
            SoundSource source = soundSourceList.get(i);
            if (!source.isPlaying()) {
                source.cleanup();
                soundSourceList.remove(source);
            }
        }
    }

    public void loadSound(String name, String filepath) {
        logger.info("Loading sound '" + filepath + "' as '" + name + "'");
        try {
            Sound sound = new Sound(filepath);
            soundBufferList.put(name, sound);
        } catch (Exception e) {
            logger.error("Error occurred while loading a sound: " + e.getMessage());
        }
    }

    public void playSound(String name) {
        if (soundBufferList.containsKey(name)) {
            Sound sound = soundBufferList.get(name);
            SoundSource soundSource = new SoundSource(false, false);
            soundSource.setBuffer(sound.getBufferId());
            soundSource.play();
            soundSourceList.add(soundSource);
        }
    }

    public void cleanup() {
        ALC10.alcMakeContextCurrent(MemoryUtil.NULL);
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }

    public void setAllActiveSoundsPlaying(boolean playing) {
        for (int i = 0; i < soundSourceList.size(); i++) {
            SoundSource source = soundSourceList.get(i);
            if (playing) {
                source.play();
            } else {
                source.pause();
            }
        }
    }
}