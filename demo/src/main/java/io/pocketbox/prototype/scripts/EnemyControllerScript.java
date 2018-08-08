package io.pocketbox.prototype.scripts;

import io.pocketbox.engine.ecs.component.AnimationComponent;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.event.EventBus;
import io.pocketbox.engine.scripting.GameObject;
import io.pocketbox.engine.scripting.Script;
import io.pocketbox.engine.sound.SoundEvent;
import io.pocketbox.prototype.objects.Apple;
import io.pocketbox.prototype.objects.Player;

import static io.pocketbox.prototype.scripts.event.GameEvent.ENEMY_KILLED;

public class EnemyControllerScript extends Script {

    private final float xEnemyVelocity = 1.2f;
    private int health = 30;
    private boolean hurting = false;

    private State state = State.RUN;

    private TransformComponent transformComponent;
    private TransformComponent targetTransferComponent;
    private RigidBodyComponent rigidBodyComponent;
    private AnimationComponent animationComponent;

    private Player player;

    public EnemyControllerScript(Player player) {
        this.targetTransferComponent = player.getComponent(TransformComponent.class);
        this.player = player;
    }

    @Override
    public void onStart() {
        transformComponent = gameObject.getComponent(TransformComponent.class);
        rigidBodyComponent = gameObject.getComponent(RigidBodyComponent.class);
        animationComponent = gameObject.getComponent(AnimationComponent.class);
        animationComponent.setAnimation("run");
    }

    @Override
    public void update() {
        if (state.equals(State.STAND)) {
            // no-ops
        }
        if (!state.equals(State.WIN) && !state.equals(State.LAUGH) && !state.equals(State.STAND)) {
            if (player.isDead()) {
                state = State.WIN;
                animationComponent.setAnimation("win");
            }
        }
        if (state.equals(State.RUN)) {
            run();
        }
        if (state.equals(State.DEATH)) {
            if (animationComponent.state.getCurrent(0).isComplete()) {
                gameObject.destroy();
            }
        }
        if (state.equals(State.HIT)) {
            if (animationComponent.state.getCurrent(0).isComplete()) {
                animationComponent.setAnimation("run");
                state = State.RUN;
            }
        }
        if (state.equals(State.ATTACK)) {
            float trackTime = animationComponent.state.getCurrent(0).getTrackTime();
            if (!hurting && trackTime > 0.4f) {
                EventBus.instance().post(new SoundEvent("sounds/enemy_attack.wav"));
                player.hurt();
                hurting = true;
            }
            if (animationComponent.state.getCurrent(0).isComplete()) {
                animationComponent.setAnimation("normalAttack");
                hurting = false;
            }
        }
        if (state.equals(State.WIN)) {
            if (animationComponent.state.getCurrent(0).isComplete()) {
                state = State.LAUGH;
                animationComponent.setAnimation("uniqueAttack");
            }
        }
        if (state.equals(State.LAUGH)) {
            if (animationComponent.state.getCurrent(0).isComplete()) {
                state = State.STAND;
                animationComponent.setAnimation("steady");
            }
        }
    }

    @Override
    public void onStartCollide(GameObject collided) {
        if (collided.getTag().equals(Apple.TAG)) {
            state = State.HIT;
            health -= 10f;
            rigidBodyComponent.body.setLinearVelocity(0f, 0f);
            if (health == 0) {
                EventBus.instance().post(ENEMY_KILLED);
                state = State.DEATH;
                rigidBodyComponent.body.setActive(false);
                animationComponent.setAnimation("dead");
                EventBus.instance().post(new SoundEvent("sounds/enemy_death.wav"));
                return;
            }
            animationComponent.setAnimation("hit");
            EventBus.instance().post(new SoundEvent("sounds/enemy_hurt.wav"));
        }
        if (collided.getTag().equals(Player.TAG)) {
            state = State.ATTACK;
            animationComponent.setAnimation("normalAttack");
            animationComponent.state.getCurrent(0).setTrackTime(0.3f);
        }
    }

    @Override
    public void onEndCollide(GameObject collided) {
        if (collided.getTag().equals(Player.TAG)) {
            state = State.RUN;
            animationComponent.setAnimation("run");
        }
    }

    private void run() {
        if (targetTransferComponent.position.x != transformComponent.position.x) {
            if (targetTransferComponent.position.x > transformComponent.position.x) {
                animationComponent.skeleton.setFlipX(false);
                rigidBodyComponent.body.setLinearVelocity(xEnemyVelocity, 0f);
            } else {
                animationComponent.skeleton.setFlipX(true);
                rigidBodyComponent.body.setLinearVelocity(-xEnemyVelocity, 0f);
            }
        }
    }

    private enum State {
        STAND, RUN, DEATH, HIT, ATTACK, WIN, LAUGH
    }
}
