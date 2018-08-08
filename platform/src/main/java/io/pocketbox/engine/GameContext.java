package io.pocketbox.engine;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.minlog.Log;
import io.pocketbox.engine.ecs.Box2DBodyListener;
import io.pocketbox.engine.ecs.component.RigidBodyComponent;
import io.pocketbox.engine.scripting.GameLogicSystem;
import io.pocketbox.engine.ecs.system.rendering.AnimationRenderingSystem;
import io.pocketbox.engine.ecs.system.rendering.ParticlesRenderingSystem;
import io.pocketbox.engine.ecs.system.rendering.TextureRenderingSystem;
import io.pocketbox.engine.ecs.system.transform.AnimationTransformSystem;
import io.pocketbox.engine.ecs.system.transform.BodyTransformSystem;
import io.pocketbox.engine.ecs.system.transform.ParticleTransformSystem;
import io.pocketbox.engine.resources.AssetsSource;
import io.pocketbox.engine.sound.MusicManager;
import io.pocketbox.engine.sound.SoundManager;

import static com.esotericsoftware.minlog.Log.LEVEL_DEBUG;
import static io.pocketbox.engine.DynamicConfig.*;

public class GameContext extends Game {

    private Class<? extends GameScreen> startScreenClass;
    public final PlatformResolver resolver;
    public final AssetsSource assetsSource;
    public final String preferencesNamespace;
    public Preferences preferences;
    public SoundManager soundManager;
    public MusicManager musicManager;
    public final PooledEngine engine;
    public Camera worldCamera;

    private PolygonSpriteBatch batch;
    private World world;
    private Viewport worldViewport;
    private Camera guiCamera;
    private Viewport guiViewport;
    private Stage stage;

    public GameContext(Class<? extends GameScreen> startScreen,
                       PlatformResolver resolver,
                       String preferencesNamespace) {
        Log.set(LEVEL_DEBUG);
        this.startScreenClass = startScreen;
        this.resolver = resolver;
        this.engine = new PooledEngine();
        this.assetsSource = new AssetsSource();
        this.preferencesNamespace = preferencesNamespace;
    }

    public void addUiElement(Actor actor) {
        this.stage.addActor(actor);
    }

    @Override
    public void create() {
        this.batch = new PolygonSpriteBatch();
        this.preferences = Gdx.app.getPreferences(preferencesNamespace);

        this.worldViewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT);
        this.worldCamera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);

        this.guiViewport = new StretchViewport(GUI_WIDTH, GUI_HEIGHT);
        this.guiCamera = new OrthographicCamera(GUI_WIDTH, GUI_HEIGHT);

        this.world = new World(new Vector2(0, -9.8f), true);
        this.stage = new Stage(guiViewport, batch);

        engine.addSystem(new BodyTransformSystem());
        engine.addSystem(new AnimationTransformSystem());
        engine.addSystem(new ParticleTransformSystem());

        engine.addSystem(new TextureRenderingSystem(batch, worldCamera));
        engine.addSystem(new AnimationRenderingSystem(batch));
        engine.addSystem(new ParticlesRenderingSystem(batch));
//        engine.addSystem(new DebugRenderingSystem(world, worldViewport));
        engine.addSystem(new GameLogicSystem(world));

        engine.addEntityListener(Family.all(RigidBodyComponent.class).get(),
                new Box2DBodyListener(world));

        this.soundManager = new SoundManager(this);
        this.musicManager = new MusicManager(this);

        try {
            setScreen(startScreenClass.getConstructor(GameContext.class).newInstance(this));
        } catch (Exception e) {
            throw new IllegalArgumentException("Start screen has'n been created", e);
        }
        Gdx.input.setInputProcessor(stage);
    }

    public void removeAllGameObjects() {
        engine.removeAllEntities();
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 60f, 6, 2);
        engine.update(Gdx.graphics.getDeltaTime());
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        worldViewport.update(width, height);
        guiViewport.update(width, height);
        guiCamera.update();
        guiCamera.position.set(
                guiCamera.viewportWidth * 0.5f,
                guiCamera.viewportHeight * 0.5f,
                0.0f);
    }
}
