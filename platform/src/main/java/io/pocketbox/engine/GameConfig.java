package io.pocketbox.engine;

public class GameConfig {
    public float worldWidth, worldHeight;
    public float guiWidth, guiHeight;
    public float pixel2meter;

    public GameConfig(float guiWidth, float guiHeight) {
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.worldWidth = guiWidth / 100f;
        this.worldHeight = guiHeight / 100f;
        this.pixel2meter = worldWidth / guiWidth;
    }
}
