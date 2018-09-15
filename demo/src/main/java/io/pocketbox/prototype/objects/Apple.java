package io.pocketbox.prototype.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.pocketbox.engine.GameConfig;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.BoxCollider;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.TextureComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.scripting.GameObject;
import io.pocketbox.prototype.scripts.AppleControllerScript;

public class Apple extends GameObject {

    public static final String TAG = "Apple";
    private final float scale = 0.2f;
    private AppleControllerScript appleControllerScript = new AppleControllerScript();

    public Apple(float xStart, float yStart, GameContext gameContext) {
        super(gameContext, TAG);
        add(new TransformComponent(xStart, yStart, scale));
        TextureRegion textureRegion = gameContext.assetsSource.getTextureRegion("images/apple.png");
        add(new TextureComponent(textureRegion));
        add(new RigidBodyComponent(new BoxCollider()
                .setType(BodyDef.BodyType.DynamicBody)
                .setDensity(0f)
                .setFriction(0.2f)
                .setRestitution(0f)
                .setAsABox(textureRegion.getRegionWidth() * GameConfig.PIXEL_2_UNIT * scale,
                        textureRegion.getRegionHeight() * GameConfig.PIXEL_2_UNIT * scale)));
        add(appleControllerScript);
        create();
    }

    public void throwToward(float direction) {
        appleControllerScript.throwToward(direction);
    }
}
