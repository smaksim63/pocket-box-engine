package io.pocketbox.prototype;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.GameScreen;
import io.pocketbox.engine.event.EventBus;
import io.pocketbox.engine.sound.MusicEvent;
import io.pocketbox.prototype.objects.Background;
import io.pocketbox.prototype.objects.GameManager;
import io.pocketbox.prototype.objects.Ground;
import io.pocketbox.prototype.objects.Player;
import io.pocketbox.engine.prototype.ui.ArrowCross;
import io.pocketbox.engine.prototype.ui.PsButtons;

public class ExampleScreen extends GameScreen {

    private ArrowCross arrowCross;
    private PsButtons psButtons;

    private ImageButton pauseButton, resetButton;
    private Image healthImage, coinImage, hurtVeil;
    private Label healthLabel, scoreLabel, gameResultLabel;

    private Player player;
    private Background background;
    private Ground ground;
    private GameManager gameManager;

    public ExampleScreen(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    public void show() {
        super.show();
        EventBus.instance().post(new MusicEvent("sounds/battle_theme.mp3", 0.5f));
        arrowCross = new ArrowCross(gameContext);
        arrowCross.setPosition(56, 50);
        arrowCross.setOnLeftPressed(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.moveLeft();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                player.stop();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        arrowCross.setOnRightPressed(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.moveRight();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                player.stop();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        gameContext.addUiElement(arrowCross);

        psButtons = new PsButtons(gameContext);
        psButtons.setPosition(982, 15);
        psButtons.setOnCrossPressed(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        player.jump();
                    }
                }
        );
        psButtons.setOnSquarePressed(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        player.throwApple();
                    }
                }
        );
        gameContext.addUiElement(psButtons);

        pauseButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/pause_button.png", Texture.class))));
        pauseButton.setPosition(490, 20);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        gameContext.addUiElement(pauseButton);

        resetButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/reset_button.png", Texture.class))));
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameContext.setScreen(new ExampleScreen(gameContext));
            }
        });
        resetButton.setPosition(605, 20);
        gameContext.addUiElement(resetButton);

        healthImage = new Image(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/health_image.png", Texture.class))));
        healthImage.setPosition(38, 675);
        gameContext.addUiElement(healthImage);

        coinImage = new Image(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/coin_image.png", Texture.class))));
        coinImage.setPosition(490, 675);
        gameContext.addUiElement(coinImage);

        hurtVeil = new Image(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/hurt_veil.png", Texture.class))
        ));
        hurtVeil.addAction(Actions.alpha(0.5f));
        hurtVeil.addAction(Actions.visible(false));
        gameContext.addUiElement(hurtVeil);

        Label.LabelStyle fontStyle = new Label.LabelStyle(
                gameContext.assetsSource.getAsset("fonts/aMavickFont48.fnt", BitmapFont.class), Color.WHITE);


        healthLabel = new Label("", fontStyle);
        healthLabel.setPosition(136, 705);
        gameContext.addUiElement(healthLabel);

        scoreLabel = new Label("", fontStyle);
        scoreLabel.setPosition(580, 705);
        gameContext.addUiElement(scoreLabel);

        gameResultLabel = new Label("", fontStyle);
        gameResultLabel.addAction(Actions.hide());
        gameContext.addUiElement(gameResultLabel);

        background = new Background(gameContext, "images/level_bg.png");
        ground = new Ground(gameContext, 0f, -3.05f, 19.20f);
        player = new Player(gameContext, healthLabel, hurtVeil);
        gameManager = new GameManager(gameContext, player, scoreLabel, gameResultLabel);
    }

    @Override
    public void hide() {
        super.hide();
        arrowCross.remove();
        psButtons.remove();
        pauseButton.remove();
        resetButton.remove();
        healthImage.remove();
        coinImage.remove();
        hurtVeil.remove();
        healthLabel.remove();
        scoreLabel.remove();
        gameResultLabel.remove();

        player.destroy();
        background.destroy();
        ground.destroy();
        gameManager.destroy();

        gameContext.removeAllGameObjects();
    }
}
