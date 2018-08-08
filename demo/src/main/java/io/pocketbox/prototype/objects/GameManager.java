package io.pocketbox.prototype.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.scripting.GameObject;
import io.pocketbox.prototype.scripts.GameManagerScript;

public class GameManager extends GameObject {

    public GameManager(GameContext gameContext,
                       Player player,
                       Label scoreLabel,
                       Label gameResultLabel) {
        super(gameContext);
        add(new GameManagerScript(gameContext, player,
                scoreLabel, gameResultLabel));
        create();
    }
}
