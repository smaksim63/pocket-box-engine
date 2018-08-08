package io.pocketbox.engine.scripting;

import com.badlogic.ashley.core.Entity;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.ScriptingComponent;

public abstract class GameObject extends Entity {

    boolean isScheduledToDelete;
    protected GameContext gameContext;
    private GameLogicSystem gameLogicSystem;
    private String tag = "Untagged";

    public GameObject(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameLogicSystem = gameContext.engine.getSystem(GameLogicSystem.class);
    }

    public GameObject(GameContext gameContext, String tag) {
        this(gameContext);
        this.tag = tag;
    }

    public Entity add(Script<?>... scripts) {
        for (Script<?> script : scripts) {
            script.setGameObject(this);
        }
        return super.add(new ScriptingComponent(scripts));
    }

    protected void create() {
        gameLogicSystem.createGameObject(this);
    }

    public void destroy() {
        isScheduledToDelete = true;
    }

    public String getTag() {
        return this.tag;
    }
}
