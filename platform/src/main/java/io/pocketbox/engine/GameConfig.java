package io.pocketbox.engine;

public class GameConfig {
    public static float WORLD_WIDTH, WORLD_HEIGHT;
    public static float GUI_WIDTH, GUI_HEIGHT;
    public static float PIXEL_2_UNIT;

    static final float HALF_TAP_SQUARE_SIZE = 20.0f;
    static final float TAP_COUNT_INTERVAL = 0.4f;
    static final float LONG_PRESS_DURATION = 1.1f;
    static final float MAX_FLING_DELAY = 0.15f;

    public GameConfig(float guiWidth, float guiHeight) {
        GUI_WIDTH = guiWidth;
        GUI_HEIGHT = guiHeight;
        WORLD_WIDTH = guiWidth / 100f;
        WORLD_HEIGHT = guiHeight / 100f;
        PIXEL_2_UNIT = WORLD_WIDTH / guiWidth;
    }
}
