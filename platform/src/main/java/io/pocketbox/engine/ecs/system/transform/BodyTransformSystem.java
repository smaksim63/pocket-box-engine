package io.pocketbox.engine.ecs.system.transform;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;

import static io.pocketbox.engine.Config.BODY_TRANSFORM_PRIORITY;

public class BodyTransformSystem extends IteratingSystem {

    private final ComponentMapper<TransformComponent> transformMapper;
    private final ComponentMapper<RigidBodyComponent> bodyMapper;

    public BodyTransformSystem() {
        super(Family.all(TransformComponent.class,
                RigidBodyComponent.class).get(), BODY_TRANSFORM_PRIORITY);
        this.transformMapper = ComponentMapper.getFor(TransformComponent.class);
        this.bodyMapper = ComponentMapper.getFor(RigidBodyComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = transformMapper.get(entity);
        RigidBodyComponent rigidBodyComponent = bodyMapper.get(entity);
        Vector2 bodyPos = rigidBodyComponent.body.getPosition();
        bodyPos.x += rigidBodyComponent.offset.x;
        bodyPos.y += rigidBodyComponent.offset.y;
        transformComponent.position.x = bodyPos.x;
        transformComponent.position.y = bodyPos.y;
    }
}
