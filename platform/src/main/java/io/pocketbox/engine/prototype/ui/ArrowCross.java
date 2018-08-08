package io.pocketbox.engine.prototype.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.pocketbox.engine.GameContext;

public class ArrowCross extends Group {

    private ImageButton leftArrowButton, rightArrowButton, upArrowButton, downArrowButton;

    public ArrowCross(GameContext gameContext) {
        leftArrowButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/left_arrow.png", Texture.class))));
        leftArrowButton.setPosition(0.f, 75.f);
        addActor(leftArrowButton);

        rightArrowButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/right_arrow.png", Texture.class))));
        rightArrowButton.setPosition(128.f, 75.f);
        addActor(rightArrowButton);

        downArrowButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/down_arrow.png", Texture.class))));
        downArrowButton.setPosition(83.f, 0.f);
        addActor(downArrowButton);

        upArrowButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/up_arrow.png", Texture.class))));
        upArrowButton.setPosition(83.f, 120.f);
        addActor(upArrowButton);
    }

    public void setOnLeftPressed(ClickListener clickListener) {
        leftArrowButton.addListener(clickListener);
    }

    public void setOnRightPressed(ClickListener clickListener) {
        rightArrowButton.addListener(clickListener);
    }

    public void setOnUpPressed(ClickListener clickListener) {
        upArrowButton.addListener(clickListener);
    }

    public void setOnDownPressed(ClickListener clickListener) {
        downArrowButton.addListener(clickListener);
    }
}
