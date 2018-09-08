package io.pocketbox.engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.pocketbox.engine.GameConfig;
import io.pocketbox.engine.GameContext;
import io.pocketbox.prototype.LoadingScreen;

public class EngineDesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 640;
        GameConfig gameConfig = new GameConfig(1280f, 768f);
        new LwjglApplication(new GameContext(LoadingScreen.class,
                new DefaultDesktopResolver(), gameConfig, "POCKET_BOX_ENGINE"), config);
    }
}
