package io.pocketbox.prototype;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.esotericsoftware.spine.SkeletonData;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.GameScreen;
import io.pocketbox.engine.resources.AnimationLoader;

public class LoadingScreen extends GameScreen {

    private Image splash;

    public LoadingScreen(GameContext gameContext) {
        super(gameContext);
        splash = new Image(new Texture("images/loading_bg.png"));
        gameContext.addUiElement(splash);
        gameContext.assetsSource.loadAssets("fonts", BitmapFont.class, "fnt");
        gameContext.assetsSource.loadAssets("images", Texture.class, "png", "jpg");
        gameContext.assetsSource.loadAssets("sounds", Music.class, "mp3");
        gameContext.assetsSource.loadAssets("sounds", Sound.class, "wav");
        gameContext.assetsSource.loadAssets("particles", ParticleEffect.class, "part");
        gameContext.assetsSource.loadAssets("i18n", I18NBundle.class, true, "properties");
        gameContext.assetsSource.loadAssets("skins", Skin.class, "json");
        gameContext.assetsSource.loadAssets("animations", TextureAtlas.class, "atlas");
        gameContext.assetsSource.loadAssets("animations/Player", SkeletonData.class, new AnimationLoader.AnimationParameter(0.2f), "json");
        gameContext.assetsSource.loadAssets("animations/Enemy", SkeletonData.class, new AnimationLoader.AnimationParameter(0.5f), "json");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (gameContext.assetsSource.continueLoading() == 1.0) {
            gameContext.setScreen(new ExampleScreen(gameContext));
            splash.remove();
        }
    }
}
