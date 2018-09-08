package io.pocketbox.engine.ecs.system.transform;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.pocketbox.engine.ecs.component.ParticleComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;

import static io.pocketbox.engine.EcsConst.PARTICLE_TRANSFORM_PRIORITY;

public class ParticleTransformSystem extends IteratingSystem {

    private final ComponentMapper<TransformComponent> transformMapper;
    private final ComponentMapper<ParticleComponent> particleMapper;

    public ParticleTransformSystem() {
        super(Family.all(TransformComponent.class, ParticleComponent.class).get(),
                PARTICLE_TRANSFORM_PRIORITY);
        this.transformMapper = ComponentMapper.getFor(TransformComponent.class);
        this.particleMapper = ComponentMapper.getFor(ParticleComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = transformMapper.get(entity);
        ParticleComponent particleComponent = particleMapper.get(entity);
        particleComponent.particleEffect.setPosition(
                transformComponent.position.x,
                transformComponent.position.y);
    }
}
