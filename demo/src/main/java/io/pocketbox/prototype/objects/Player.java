package io.pocketbox.prototype.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.spine.SkeletonData;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.AnimationComponent;
import io.pocketbox.engine.ecs.component.BoxCollider;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.scripting.GameObject;
import io.pocketbox.prototype.scripts.MovementCameraScript;
import io.pocketbox.prototype.scripts.PlayerControllerScript;
import io.pocketbox.prototype.scripts.PlayerHealthScript;

public class Player extends GameObject {

    private final PlayerControllerScript controllerScript;
    private final PlayerHealthScript playerHealthScript;
    private final MovementCameraScript cameraScript;
    public static final String TAG = "Player";

    public Player(GameContext gameContext, Label label, Image hurtVeil) {
        super(gameContext, TAG);
        this.controllerScript = new PlayerControllerScript(gameContext);
        this.playerHealthScript = new PlayerHealthScript(label, hurtVeil);
        this.cameraScript = new MovementCameraScript(gameContext,
                gameContext.worldCamera);
        add(new TransformComponent(0.f, -2.5f));
        add(new AnimationComponent(gameContext.assetsSource.getAsset("animations/Player/Player.json", SkeletonData.class), "stand"));
        add(new RigidBodyComponent(new BoxCollider()
                .setType(BodyDef.BodyType.DynamicBody)
                .setDensity(0f)
                .setFriction(0.2f)
                .setRestitution(0f)
                .setAsABox(0.2f, 0.65f), 0, -0.65f));
        add(controllerScript, cameraScript, playerHealthScript);
        create();
    }

    public boolean isDead() {
        return playerHealthScript.isDead();
    }

    public void hurt() {
        playerHealthScript.hurt();
    }

    public void moveRight() {
        controllerScript.moveRight();
    }

    public void moveLeft() {
        controllerScript.moveLeft();
    }

    public void stop() {
        controllerScript.stop();
    }

    public void jump() {
        controllerScript.jump();
    }

    public void throwApple() {
        controllerScript.throwApple();
    }
}
