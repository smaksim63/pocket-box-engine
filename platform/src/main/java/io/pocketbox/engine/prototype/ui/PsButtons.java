package io.pocketbox.engine.prototype.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.pocketbox.engine.GameContext;

public class PsButtons extends Group {

    private ImageButton crossButton, squareButton, triangleButton, circleButton;

    public PsButtons(GameContext gameContext) {
        crossButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/cross_button.png", Texture.class))));
        crossButton.setPosition(90f, 0f);
        addActor(crossButton);

        squareButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/square_button.png", Texture.class))));
        squareButton.setPosition(0f, 100f);
        addActor(squareButton);

        triangleButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/triangle_button.png", Texture.class))));
        triangleButton.setPosition(90f, 200f);
        addActor(triangleButton);

        circleButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(gameContext.assetsSource.getAsset("images/ui/circle_button.png", Texture.class))));
        circleButton.setPosition(180f, 100f);
        addActor(circleButton);
    }

    public void setOnCrossPressed(ClickListener clickListener) {
        crossButton.addListener(clickListener);
    }

    public void setOnCirclePressed(ClickListener clickListener) {
        circleButton.addListener(clickListener);
    }

    public void setOnSquarePressed(ClickListener clickListener) {
        squareButton.addListener(clickListener);
    }

    public void setOnTrianglePressed(ClickListener clickListener) {
        triangleButton.addListener(clickListener);
    }
}
