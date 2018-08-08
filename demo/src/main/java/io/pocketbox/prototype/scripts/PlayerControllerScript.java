package io.pocketbox.prototype.scripts;

import com.badlogic.gdx.Gdx;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.AnimationComponent;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.event.EventBus;
import io.pocketbox.engine.scripting.Script;
import io.pocketbox.engine.sound.SoundEvent;
import io.pocketbox.prototype.objects.Apple;

public class PlayerControllerScript extends Script {

    private RigidBodyComponent rigidBodyComponent;
    private AnimationComponent animationComponent;
    private TransformComponent transformComponent;

    private State state = State.STAND;

    private final float velocity = 2f;
    private float lookDirection = -1f;
    private float linearVelocityX;
    private final float jumpPower = 250f;

    private GameContext gameContext;

    private float turnAccumulator = 0.0f;
    private final float turnDelay = 1.6f;

    public PlayerControllerScript(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void onStart() {
        this.rigidBodyComponent = gameObject.getComponent(RigidBodyComponent.class);
        this.animationComponent = gameObject.getComponent(AnimationComponent.class);
        this.transformComponent = gameObject.getComponent(TransformComponent.class);
    }

    @Override
    public void update() {
        if (state.equals(State.WALK)) {
            move();
        }
        if (state.equals(State.JUMP)) {
            checkIfOnGround();
        }
        if (state.equals(State.THROW)) {
            if (animationComponent.state.getCurrent(0).isComplete()) {
                state = State.STAND;
                animationComponent.setAnimation("stand");
            }
        }
        if (state.equals(State.STAND)) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            turnAccumulator += deltaTime;
            if (turnAccumulator > turnDelay) {
                state = State.TURN_FACE;
                animationComponent.setAnimation("turn face");
            }
        }
        if (state.equals(State.TURN_FACE)) {
            if (animationComponent.state.getCurrent(0).isComplete()) {
                state = State.STAND;
                animationComponent.setAnimation("stand");
                turnAccumulator = 0.0f;
            }
        }
    }

    public void moveRight() {
        lookDirection = 1f;
        state = State.WALK;
        animationComponent.setAnimation("walk");
        animationComponent.skeleton.setFlipX(true);
        linearVelocityX = velocity;
        move();
    }

    public void moveLeft() {
        lookDirection = -1f;
        state = State.WALK;
        animationComponent.setAnimation("walk");
        animationComponent.skeleton.setFlipX(false);
        linearVelocityX = -velocity;
        move();
    }

    public void stop() {
        linearVelocityX = 0f;
        state = State.STAND;
        animationComponent.setAnimation("stand");
        move();
    }

    public void jump() {
        if (rigidBodyComponent.body.getLinearVelocity().y == 0.0f) {
            EventBus.instance().post(new SoundEvent("sounds/jump.wav"));
            state = State.JUMP;
            rigidBodyComponent.body.setLinearVelocity(rigidBodyComponent.body.getLinearVelocity().x, 0f);
            rigidBodyComponent.body.applyForceToCenter(linearVelocityX, jumpPower, true);
        }
    }

    public void throwApple() {
        EventBus.instance().post(new SoundEvent("sounds/player_attack.wav"));
        state = State.THROW;
        animationComponent.setAnimation("atc");
        Apple apple = new Apple(transformComponent.position.x + (0.5f * lookDirection),
                transformComponent.position.y + 1f, gameContext);
        apple.throwToward(lookDirection);
    }

    private void move() {
        rigidBodyComponent.body.setLinearVelocity(linearVelocityX, rigidBodyComponent.body.getLinearVelocity().y);
    }

    private void checkIfOnGround() {
        if (rigidBodyComponent.body.getLinearVelocity().y == 0.f) {
            state = State.WALK;
        }
    }

    private enum State {
        STAND, WALK, JUMP, THROW, TURN_FACE
    }
}
