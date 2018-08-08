package io.pocketbox.engine;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import io.pocketbox.prototype.LoadingScreen;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new GameContext(LoadingScreen.class, new DefaultAndroidResolver(), "POCKET_BOX_ENGINE"), config);
    }
}
