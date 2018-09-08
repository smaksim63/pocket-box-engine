package io.pocketbox.engine.ecs.system.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import io.pocketbox.engine.ecs.component.AnimationComponent;

import static io.pocketbox.engine.EcsConst.DEBUG_RENDERING_PRIORITY;

public class DebugRenderingSystem extends EntitySystem {

    private final World world;
    private final Viewport viewport;
    private final SkeletonRendererDebug debugAnimationRenderer;
    private final Box2DDebugRenderer debugBox2DRenderer;
    private final ComponentMapper<AnimationComponent> animationMapper;

    public DebugRenderingSystem(World world, Viewport viewport) {
        super(DEBUG_RENDERING_PRIORITY);
        this.world = world;
        this.viewport = viewport;

        this.debugAnimationRenderer = new SkeletonRendererDebug();
        this.debugAnimationRenderer.setBones(true);
        this.debugAnimationRenderer.setRegionAttachments(true);
        this.debugAnimationRenderer.setBoundingBoxes(true);
        this.debugAnimationRenderer.setMeshHull(true);
        this.debugAnimationRenderer.setMeshTriangles(true);

        this.debugBox2DRenderer = new Box2DDebugRenderer(
                true, /* draw bodies */
                false, /* don't draw joints */
                true, /* draw aabbs */
                true, /* draw inactive bodies */
                false, /* don't draw velocities */
                true /* draw contacts */);

        this.animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        debugBox2DRenderer.render(world, viewport.getCamera().combined);
//        ImmutableArray<Entity> entities = getEngine()
//                .getEntitiesFor(Family.all(AnimationComponent.class).get());
//        for (Entity entity : entities) {
//            AnimationComponent animationComponent = animationMapper.get(entity);
//            debugAnimationRenderer.draw(animationComponent.skeleton);
//        }
    }
}
