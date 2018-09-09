package io.pocketbox.engine.scripting;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.minlog.Log;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.ecs.component.ScriptingComponent;
import io.pocketbox.engine.event.EventBus;

import java.util.HashMap;
import java.util.Map;

import static io.pocketbox.engine.EcsConst.GAME_LOGIC_SYSTEM_PRIORITY;

public class GameLogicSystem extends IteratingSystem implements ContactListener, GestureDetector.GestureListener {

    private final Camera camera;
    private final World world;
    private final Array<Entity> entities;
    private final Array<Entity> toDestroy;
    private final Map<Body, PanEvent> panEvents;
    private final Map<Body, TouchDownEvent> touchDownEvents;
    private final Map<GameObject, Array<Script<?>>> startCollideA;
    private final Map<GameObject, Array<Script<?>>> startCollideB;
    private final Map<GameObject, Array<Script<?>>> endCollideA;
    private final Map<GameObject, Array<Script<?>>> endCollideB;
    private final ComponentMapper<ScriptingComponent> scriptingMapper;

    public GameLogicSystem(Camera camera, World world) {
        super(Family.all(ScriptingComponent.class).get(), GAME_LOGIC_SYSTEM_PRIORITY);
        this.camera = camera;
        this.world = world;
        this.entities = new Array<>();
        this.toDestroy = new Array<>();
        this.panEvents = new HashMap<>();
        this.touchDownEvents = new HashMap<>();
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
        touchDown();
        pan();
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

    private void touchDown() {
        for (Entity entity : entities) {
            RigidBodyComponent rigidBodyComponent = entity.getComponent(RigidBodyComponent.class);
            if (rigidBodyComponent != null) {
                if (touchDownEvents.containsKey(rigidBodyComponent.body)) {
                    ScriptingComponent scriptingComponent = scriptingMapper.get(entity);
                    TouchDownEvent event = touchDownEvents.get(rigidBodyComponent.body);
                    for (Script<?> script : scriptingComponent.scripts) {
                        script.pan(event.x, event.y, event.pointer, event.button);
                    }
                    touchDownEventPool.free(event);
                }
            }
        }
        touchDownEvents.clear();
    }

    private void pan() {
        for (Entity entity : entities) {
            RigidBodyComponent rigidBodyComponent = entity.getComponent(RigidBodyComponent.class);
            if (rigidBodyComponent != null) {
                if (panEvents.containsKey(rigidBodyComponent.body)) {
                    ScriptingComponent scriptingComponent = scriptingMapper.get(entity);
                    PanEvent event = panEvents.get(rigidBodyComponent.body);
                    for (Script<?> script : scriptingComponent.scripts) {
                        script.pan(event.x, event.y, event.deltaX, event.deltaY);
                    }
                    panEventPool.free(event);
                }
            }
        }
        panEvents.clear();
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
    public boolean touchDown(final float x, final float y,
                             final int pointer, final int button) {
        Log.debug("touchDown", String.format("screen x=%s y=%s", x, y));
        final Vector3 touchPos = new Vector3(x, y, 0f);
        camera.unproject(touchPos);
        world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                Log.debug("reportFixture", String.format("x=%s y=%s", x, y));
                TouchDownEvent event = touchDownEventPool.obtain();
                event.x = x;
                event.y = y;
                event.pointer = pointer;
                event.button = button;
                touchDownEvents.put(fixture.getBody(), event);
                return false;
            }
        }, touchPos.x, touchPos.y, touchPos.x, touchPos.y);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(final float x, final float y,
                       final float deltaX, final float deltaY) {
        Log.debug("pan", String.format("screen x=%s y=%s", x, y));
        final Vector3 touchPos = new Vector3(x, y, 0f);
        camera.unproject(touchPos);
        world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                Log.debug("reportFixture", String.format("x=%s y=%s", x, y));
                PanEvent event = panEventPool.obtain();
                event.x = x;
                event.y = y;
                event.deltaX = deltaX;
                event.deltaY = deltaY;
                panEvents.put(fixture.getBody(), event);
                return false;
            }
        }, touchPos.x, touchPos.y, touchPos.x, touchPos.y);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private final Pool<TouchDownEvent> touchDownEventPool = new Pool<TouchDownEvent>() {
        @Override
        protected TouchDownEvent newObject() {
            return new TouchDownEvent();
        }
    };

    private final Pool<PanEvent> panEventPool = new Pool<PanEvent>() {
        @Override
        protected PanEvent newObject() {
            return new PanEvent();
        }
    };

    private class TouchDownEvent implements Pool.Poolable {
        public float x, y;
        public int pointer, button;

        public TouchDownEvent() {
        }

        @Override
        public void reset() {
            this.x = 0f;
            this.y = 0f;
            this.pointer = 0;
            this.button = 0;
        }
    }

    private class PanEvent implements Pool.Poolable {
        float x, y;
        float deltaX, deltaY;

        public PanEvent() {
        }

        @Override
        public void reset() {
            this.x = 0f;
            this.y = 0f;
            this.deltaX = 0f;
            this.deltaY = 0f;
        }
    }
}
