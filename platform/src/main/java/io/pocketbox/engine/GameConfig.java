package io.pocketbox.engine;

public class GameConfig {
    public float worldWidth, worldHeight;
    public float guiWidth, guiHeight;
    public float pixel2meter;

    static final float HALF_TAP_SQUARE_SIZE = 20.0f;
    static final float TAP_COUNT_INTERVAL = 0.4f;
    static final float LONG_PRESS_DURATION = 1.1f;
    static final float MAX_FLING_DELAY = 0.15f;

    public GameConfig(float guiWidth, float guiHeight) {
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.worldWidth = guiWidth / 100f;
        this.worldHeight = guiHeight / 100f;
        this.pixel2meter = worldWidth / guiWidth;
    }
}
