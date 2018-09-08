package io.pocketbox.engine.ecs.system.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.pocketbox.engine.ecs.component.ParticleComponent;

import static io.pocketbox.engine.EcsConst.PARTICLE_RENDERING_PRIORITY;

public class ParticlesRenderingSystem extends IteratingSystem {

    private final PolygonSpriteBatch batch;
    private final Array<Entity> entities;
    private final ComponentMapper<ParticleComponent> particleMapper;

    public ParticlesRenderingSystem(PolygonSpriteBatch spriteBatch) {
        super(Family.all(ParticleComponent.class).get(), PARTICLE_RENDERING_PRIORITY);
        this.batch = spriteBatch;
        this.entities = new Array<>();
        this.particleMapper = ComponentMapper.getFor(ParticleComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : entities) {
            ParticleComponent particleComponent = particleMapper.get(entity);
            particleComponent.particleEffect.draw(batch, deltaTime);
        }
        batch.end();
        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.entities.add(entity);
    }
}
