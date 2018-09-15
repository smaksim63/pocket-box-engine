package io.pocketbox.prototype.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.esotericsoftware.spine.SkeletonData;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.AnimationComponent;
import io.pocketbox.engine.ecs.component.BoxCollider;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.scripting.GameObject;
import io.pocketbox.prototype.scripts.EnemyControllerScript;

public class Enemy extends GameObject {

    public static final String TAG = "Enemy";

    public Enemy(GameContext gameContext,
                 Player target,
                 float xSpawnPosition,
                 float ySpawnPosition) {
        super(gameContext, TAG);
        add(new TransformComponent(xSpawnPosition, ySpawnPosition));
        add(new AnimationComponent(gameContext.assetsSource.getAsset("animations/Enemy/Enemy.json", SkeletonData.class), "steady"));
        add(new RigidBodyComponent(new BoxCollider()
                .setType(BodyDef.BodyType.DynamicBody)
                .setDensity(0f)
                .setFriction(0.2f)
                .setRestitution(0f)
                .setAsABox(0.4f, 1f), 0, -0.42f));
        add(new EnemyControllerScript(target));
        create();
    }
}
