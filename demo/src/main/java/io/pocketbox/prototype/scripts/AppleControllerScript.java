package io.pocketbox.prototype.scripts;

import com.badlogic.gdx.Gdx;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.scripting.Script;

public class AppleControllerScript extends Script {

    private RigidBodyComponent rigidBodyComponent;
    private final float throwVelocity = 10.f;
    private final float lifeTimeLimit = 0.4f;
    private float lifeTime;

    @Override
    public void onStart() {
        rigidBodyComponent = gameObject.getComponent(RigidBodyComponent.class);
    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        lifeTime += delta;
        if (lifeTime > lifeTimeLimit) {
            gameObject.destroy();
        }
    }

    public void throwToward(float direction) {
        rigidBodyComponent.body.setLinearVelocity(direction * throwVelocity, 0.0f);
    }
}
