package io.pocketbox.engine.scripting;

import com.badlogic.ashley.core.Entity;

public abstract class Script<E> {

    protected GameObject gameObject;
    boolean isInitialized;

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public Entity getGameObject() {
        return gameObject;
    }

    public void onStart() {
    }

    public void onStartCollide(GameObject collided) {
    }

    public void onEndCollide(GameObject collided) {
    }

    public void update() {
    }

    public void onDestroy() {
    }

    public void onMessage(E event) {
    }
}
