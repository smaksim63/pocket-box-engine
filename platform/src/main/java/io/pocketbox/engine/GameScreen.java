package io.pocketbox.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import io.pocketbox.engine.scripting.GameLogicSystem;

public abstract class GameScreen extends ScreenAdapter {

    protected GameContext gameContext;

    public GameScreen(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void show() {
        gameContext.engine.getSystem(GameLogicSystem.class).update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void hide() {
        gameContext.engine.getSystem(GameLogicSystem.class).update(Gdx.graphics.getDeltaTime());
    }
}
