package io.pocketbox.engine.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.ScriptingComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.scripting.GameObject;

public class Box2DBodyListener implements EntityListener {

    private final World world;
    private final ComponentMapper<RigidBodyComponent> rigidBodyMapper;
    private final ComponentMapper<TransformComponent> transformMapper;
    private final ComponentMapper<ScriptingComponent> scriptingMapper;

    public Box2DBodyListener(World world) {
        this.world = world;
        this.rigidBodyMapper = ComponentMapper.getFor(RigidBodyComponent.class);
        this.transformMapper = ComponentMapper.getFor(TransformComponent.class);
        this.scriptingMapper = ComponentMapper.getFor(ScriptingComponent.class);
    }

    @Override
    public void entityAdded(Entity entity) {
        RigidBodyComponent rigidBodyComponent = this.rigidBodyMapper.get(entity);
        TransformComponent transformComponent = this.transformMapper.get(entity);

        BodyDef bodyDef = rigidBodyComponent.boxCollider.getBodyDef();

        if (transformComponent == null) {
            transformComponent = new TransformComponent();
            entity.add(transformComponent);
        }

        bodyDef.position.set(transformComponent.position.x, transformComponent.position.y);

        FixtureDef fixtureDef = rigidBodyComponent.boxCollider.getFixtureDef();
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        rigidBodyComponent.body = body;
        GameObject gameObject = (GameObject) entity;
        body.setUserData(gameObject);
    }

    @Override
    public void entityRemoved(Entity entity) {
        RigidBodyComponent rigidBodyComponent = this.rigidBodyMapper.get(entity);
        world.destroyBody(rigidBodyComponent.body);
    }
}
