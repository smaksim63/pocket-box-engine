package io.pocketbox.engine.ecs.system.transform;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.ecs.component.AnimationComponent;

import static io.pocketbox.engine.Config.ANIMATION_TRANSFORM_PRIORITY;

public class AnimationTransformSystem extends IteratingSystem {

    private final ComponentMapper<TransformComponent> transformMapper;
    private final ComponentMapper<AnimationComponent> animationMapper;

    public AnimationTransformSystem() {
        super(Family.all(TransformComponent.class, AnimationComponent.class).get(),
                ANIMATION_TRANSFORM_PRIORITY);
        this.transformMapper = ComponentMapper.getFor(TransformComponent.class);
        this.animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = transformMapper.get(entity);
        AnimationComponent animationComponent = animationMapper.get(entity);
        animationComponent.skeleton.setPosition(
                transformComponent.position.x,
                transformComponent.position.y
        );
    }
}
