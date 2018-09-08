package io.pocketbox.engine.ecs.system.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.TextureComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;

import java.util.Comparator;

import static io.pocketbox.engine.EcsConst.*;

public class TextureRenderingSystem extends IteratingSystem {

    private final GameContext gameContext;
    private final PolygonSpriteBatch batch;
    private final Camera camera;
    private final Array<Entity> queue;
    private final Comparator<Entity> comparator;

    private final ComponentMapper<TextureComponent> textureMapper;
    private final ComponentMapper<TransformComponent> transformMapper;

    public TextureRenderingSystem(GameContext gameContext,
                                  PolygonSpriteBatch batch,
                                  Camera camera) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), TEXTURE_RENDERING_PRIORITY);
        this.gameContext = gameContext;
        this.batch = batch;
        this.camera = camera;
        this.queue = new Array<>();
        this.comparator = new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {
                return (int) Math.signum(transformMapper.get(entityB).position.z -
                        transformMapper.get(entityA).position.z);
            }
        };
        this.textureMapper = ComponentMapper.getFor(TextureComponent.class);
        this.transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.camera.update();
        this.batch.setProjectionMatrix(this.camera.combined);
        queue.sort(comparator);
        batch.begin();
        for (Entity entity : queue) {
            renderTexture(entity);
        }
        queue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }

    private void renderTexture(Entity entity) {
        TextureComponent textureComponent = textureMapper.get(entity);
        TransformComponent transformComponent = transformMapper.get(entity);
        float width = textureComponent.texture.getRegionWidth() * gameContext.gameConfig.pixel2meter;
        float height = textureComponent.texture.getRegionHeight() * gameContext.gameConfig.pixel2meter;
        float originX = width * 0.5f;
        float originY = height * 0.5f;
        batch.draw(textureComponent.texture,
                transformComponent.position.x - originX,
                transformComponent.position.y - originY,
                originX, originY,
                width, height,
                transformComponent.scale.x, transformComponent.scale.y,
                MathUtils.radiansToDegrees * transformComponent.rotation);
    }
}
