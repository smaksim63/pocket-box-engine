package io.pocketbox.engine.scripting;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import io.pocketbox.engine.ecs.component.ScriptingComponent;
import io.pocketbox.engine.event.EventBus;

import java.util.HashMap;
import java.util.Map;

import static io.pocketbox.engine.EcsConst.GAME_LOGIC_SYSTEM_PRIORITY;

public class GameLogicSystem extends IteratingSystem implements ContactListener {

    private final Array<Entity> entities;
    private final Array<Entity> toDestroy;
    private final Map<GameObject, Array<Script<?>>> startCollideA;
    private final Map<GameObject, Array<Script<?>>> startCollideB;
    private final Map<GameObject, Array<Script<?>>> endCollideA;
    private final Map<GameObject, Array<Script<?>>> endCollideB;
    private final ComponentMapper<ScriptingComponent> scriptingMapper;

    public GameLogicSystem(World world) {
        super(Family.all(ScriptingComponent.class).get(), GAME_LOGIC_SYSTEM_PRIORITY);
        this.entities = new Array<>();
        this.toDestroy = new Array<>();
        this.startCollideA = new HashMap<>();
        this.startCollideB = new HashMap<>();
        this.endCollideA = new HashMap<>();
        this.endCollideB = new HashMap<>();
        this.scriptingMapper = ComponentMapper.getFor(ScriptingComponent.class);
        world.setContactListener(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        onStart();
        onStartCollide();
        onEndCollide();
        onMessage();
        update();
        onDestroy();
        entities.clear();
    }

    public void createGameObject(GameObject gameObject) {
        getEngine().addEntity(gameObject);
        onStart(gameObject);
    }

    private void onStart() {
        for (Entity entity : entities) {
            onStart(entity);
        }
    }

    private void onStart(Entity entity) {
        ScriptingComponent scriptingComponent = scriptingMapper.get(entity);
        if (scriptingComponent != null && scriptingComponent.scripts != null) {
            for (Script<?> script : scriptingComponent.scripts) {
                if (!script.isInitialized) {
                    script.onStart();
                    script.isInitialized = true;
                }
            }
        }
    }

    private void onStartCollide() {
        for (GameObject gameObject : startCollideA.keySet()) {
            Array<Script<?>> scripts = startCollideA.get(gameObject);
            for (Script<?> script : scripts) {
                script.onStartCollide(gameObject);
            }
        }
        for (GameObject gameObject : startCollideB.keySet()) {
            Array<Script<?>> scripts = startCollideB.get(gameObject);
            for (Script<?> script : scripts) {
                script.onStartCollide(gameObject);
            }
        }
        startCollideA.clear();
        startCollideB.clear();
    }

    private void onEndCollide() {
        for (GameObject gameObject : endCollideA.keySet()) {
            Array<Script<?>> scripts = endCollideA.get(gameObject);
            for (Script<?> script : scripts) {
                script.onEndCollide(gameObject);
            }
        }
        for (GameObject gameObject : endCollideB.keySet()) {
            Array<Script<?>> scripts = endCollideB.get(gameObject);
            for (Script<?> script : scripts) {
                script.onEndCollide(gameObject);
            }
        }
        endCollideA.clear();
        endCollideB.clear();
    }

    private void onMessage() {
        EventBus.instance().process();
    }

    private void update() {
        for (Entity entity : entities) {
            ScriptingComponent scriptingComponent = scriptingMapper.get(entity);
            for (Script<?> script : scriptingComponent.scripts) {
                script.update();
            }
        }
    }

    private void onDestroy() {
        for (Entity entity : entities) {
            GameObject gameObject = (GameObject) entity;
            if (gameObject.isScheduledToDelete) {
                ScriptingComponent scriptingComponent = scriptingMapper.get(entity);
                for (Script<?> script : scriptingComponent.scripts) {
                    script.onDestroy();
                }
                toDestroy.add(gameObject);
            }
        }
        for (Entity entity : toDestroy) {
            getEngine().removeEntity(entity);
        }
        toDestroy.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.entities.add(entity);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        GameObject obj1 = (GameObject) bodyA.getUserData();
        GameObject obj2 = (GameObject) bodyB.getUserData();

        if (obj1 != null) {
            ScriptingComponent scriptingComponent1 = scriptingMapper.get(obj1);
            if (scriptingComponent1 != null) {
                Array<Script<?>> scripts1 = scriptingComponent1.scripts;
                if (scripts1 != null) {
                    this.startCollideB.put(obj2, scripts1);
                }
            }
        }

        if (obj2 != null) {
            ScriptingComponent scriptingComponent2 = scriptingMapper.get(obj2);
            if (scriptingComponent2 != null) {
                Array<Script<?>> scripts2 = scriptingComponent2.scripts;
                if (scripts2 != null) {
                    this.startCollideA.put(obj1, scripts2);
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        GameObject obj1 = (GameObject) bodyA.getUserData();
        GameObject obj2 = (GameObject) bodyB.getUserData();

        if (obj1 != null) {
            ScriptingComponent scriptingComponent1 = scriptingMapper.get(obj1);
            if (scriptingComponent1 != null) {
                Array<Script<?>> scripts1 = scriptingComponent1.scripts;
                if (scripts1 != null) {
                    this.endCollideB.put(obj2, scripts1);
                }
            }
        }

        if (obj2 != null) {
            ScriptingComponent scriptingComponent2 = scriptingMapper.get(obj2);
            if (scriptingComponent2 != null) {
                Array<Script<?>> scripts2 = scriptingComponent2.scripts;
                if (scripts2 != null) {
                    this.endCollideA.put(obj1, scripts2);
                }
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
