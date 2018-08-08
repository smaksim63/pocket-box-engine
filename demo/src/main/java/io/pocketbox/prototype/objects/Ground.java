package io.pocketbox.prototype.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.BoxCollider;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.scripting.GameObject;

public class Ground extends GameObject {
    public Ground(GameContext gameContext,
                  float xPos, float yPos, float width) {
        super(gameContext);
        add(new TransformComponent(xPos, yPos));
        add(new RigidBodyComponent(new BoxCollider()
                .setType(BodyDef.BodyType.StaticBody)
                .setAsABox(width, 0.01f)));
        create();
    }
}
