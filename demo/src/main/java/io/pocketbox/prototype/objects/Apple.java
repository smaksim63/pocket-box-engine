package io.pocketbox.prototype.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.BoxCollider;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.TextureComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.scripting.GameObject;
import io.pocketbox.prototype.scripts.AppleControllerScript;

public class Apple extends GameObject {

    public static final String TAG = "Apple";
    private AppleControllerScript appleControllerScript = new AppleControllerScript();

    public Apple(float xStart, float yStart, GameContext gameContext) {
        super(gameContext, TAG);
        add(new TransformComponent(xStart, yStart, 0.2f));
        add(new TextureComponent(gameContext.assetsSource.getTextureRegion("images/apple.png")));
        add(new RigidBodyComponent(new BoxCollider()
                .setType(BodyDef.BodyType.DynamicBody)
                .setDensity(0f)
                .setFriction(0.2f)
                .setRestitution(0f)
                .setAsABox(0.05f, 0.05f)));
        add(appleControllerScript);
        create();
    }

    public void throwToward(float direction) {
        appleControllerScript.throwToward(direction);
    }
}
