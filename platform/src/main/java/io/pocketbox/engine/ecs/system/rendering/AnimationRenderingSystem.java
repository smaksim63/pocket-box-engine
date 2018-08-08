package io.pocketbox.engine.ecs.system.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonRenderer;
import io.pocketbox.engine.ecs.component.AnimationComponent;

import static io.pocketbox.engine.Config.ANIMATION_RENDERING_PRIORITY;

public class AnimationRenderingSystem extends IteratingSystem {

    private final PolygonSpriteBatch batch;
    private final SkeletonRenderer renderer;

    private final Array<Entity> entities;
    private final ComponentMapper<AnimationComponent> animationMapper;

    public AnimationRenderingSystem(PolygonSpriteBatch batch) {
        super(Family.all(AnimationComponent.class).get(), ANIMATION_RENDERING_PRIORITY);
        this.batch = batch;
        this.renderer = new SkeletonRenderer();
        this.entities = new Array<>();
        this.animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : entities) {
            AnimationComponent animationComponent = animationMapper.get(entity);
            animationComponent.state.update(deltaTime);
            animationComponent.state.apply(animationComponent.skeleton);
            animationComponent.skeleton.updateWorldTransform();
            renderer.draw(batch, animationComponent.skeleton);
        }
        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.entities.add(entity);
    }
}
