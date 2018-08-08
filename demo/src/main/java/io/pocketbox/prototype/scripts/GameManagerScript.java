package io.pocketbox.prototype.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.event.EventBus;
import io.pocketbox.engine.scripting.Script;
import io.pocketbox.engine.sound.MusicEvent;
import io.pocketbox.engine.sound.SoundEvent;
import io.pocketbox.prototype.objects.Enemy;
import io.pocketbox.prototype.objects.Player;
import io.pocketbox.prototype.scripts.event.GameEvent;

import static io.pocketbox.engine.DynamicConfig.*;
import static io.pocketbox.prototype.scripts.event.GameEvent.ENEMY_KILLED;
import static io.pocketbox.prototype.scripts.event.GameEvent.PLAYER_KILLED;

public class GameManagerScript extends Script<GameEvent> {

    private GameContext gameContext;
    private Label scoreCounter;
    private Label gameResultLabel;

    private int currentScores;
    private boolean win;
    private int enemyTargetCounter = 15;
    private int enemyInSameTime = 3;
    private int currentEnemyCounter = 0;
    private float delayBetweenRespawn = 1.0f;
    private float delayAccumulator = 0.0f;

    private float spawnPointY = -2.5f;
    private float spawnPointLeft = -9.3f;
    private float spawnPointRight = 9.3f;

    private Player player;

    private TransformComponent playerPosition;

    public GameManagerScript(GameContext gameContext,
                             Player player,
                             Label scoreCounter,
                             Label gameResultLabel) {
        this.gameContext = gameContext;
        this.player = player;
        this.scoreCounter = scoreCounter;
        this.gameResultLabel = gameResultLabel;
    }

    private void updateScoreLabel() {
        scoreCounter.setText(String.valueOf(currentScores));
    }

    @Override
    public void onStart() {
        EventBus.instance().register(this);
        updateScoreLabel();
        playerPosition = player.getComponent(TransformComponent.class);
    }


    @Override
    public void onMessage(GameEvent event) {
        if (event.equals(ENEMY_KILLED)) {
            currentEnemyCounter--;
            currentScores += 10;
            updateScoreLabel();
        }
        if (event.equals(PLAYER_KILLED)) {
            EventBus.instance().post(new MusicEvent("sounds/game_over.mp3"));
            drawResult("Game Over");
        }
    }

    private void drawResult(String message) {
        gameResultLabel.setText(message);
        gameResultLabel.setPosition(GUI_WIDTH * 0.5f - gameResultLabel.getPrefWidth() * 0.5f,
                GUI_HEIGHT * 0.5f - gameResultLabel.getPrefHeight() * 0.5f + 200f);
        gameResultLabel.addAction(Actions.show());
    }

    @Override
    public void update() {
        if (!win) {
            if (enemyTargetCounter == 0) {
                if (currentEnemyCounter == 0) {
                    EventBus.instance().post(new SoundEvent("sounds/level_complete.wav"));
                    win = true;
                    drawResult("Winner!");
                }
            }

            if (enemyTargetCounter != 0) {
                float deltaTime = Gdx.graphics.getDeltaTime();
                delayAccumulator += deltaTime;
                if (currentEnemyCounter + 1 <= enemyInSameTime && delayAccumulator > delayBetweenRespawn) {
                    float leftDistance = Math.abs(spawnPointLeft - playerPosition.position.x);
                    float rightDistance = Math.abs(spawnPointRight - playerPosition.position.x);
                    if (leftDistance > rightDistance) {
                         new Enemy(gameContext, player, spawnPointLeft, spawnPointY);
                    } else {
                        new Enemy(gameContext, player, spawnPointRight, spawnPointY);
                    }
                    currentEnemyCounter++;
                    enemyTargetCounter--;
                    delayAccumulator = 0.0f;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.instance().unregister(this);
    }
}
