package net.opencubes.world.level;

import net.opencubes.client.OpenCubes;

import java.util.ArrayList;

public class LevelGenerationSystem {
    private static ArrayList<ChunkPos> chunks = new ArrayList<>();
    private static ArrayList<GenerationThread> threads = new ArrayList<>();
    private static final int THREADS = 3;
    private static final int CHUNKS_PER_THREAD = 3;

    public static void start(Level level) {
        for (int i = 0; i < THREADS; i++) {
            GenerationThread thread = new GenerationThread(level);
            thread.start();
            threads.add(thread);
        }
    }

    public static void tick() {
        if (chunks.size() != 0) {
            for (GenerationThread thread : threads) {
                if (thread.chunks.size() == 0) {
                    if (chunks.size() >= CHUNKS_PER_THREAD) {
                        for (int i = 0; i < CHUNKS_PER_THREAD; i++) {
                            thread.chunks.add(chunks.remove(0));
                        }
                    } else {
                        thread.chunks.addAll(chunks);
                    }
                }
            }
        }
    }

    public static void addChunk(ChunkPos pos) {
        if (!chunks.contains(pos)) {
            chunks.add(pos);
        }
    }

    private static class GenerationThread extends Thread {
        private final Level level;
        private ArrayList<ChunkPos> chunks = new ArrayList<>();

        public GenerationThread(Level level) {
            this.level = level;
        }

        @Override
        public void run() {
            while (OpenCubes.getInstance().isRunning()) {
                if (chunks.size() != 0) {
                    ChunkPos chunk = chunks.remove(0);
                    if (chunk != null) {
                        level.generateChunk(chunk);
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}