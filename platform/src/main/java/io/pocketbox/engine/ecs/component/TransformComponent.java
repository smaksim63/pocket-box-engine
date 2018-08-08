package io.pocketbox.engine.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent implements Component {
    public Vector3 position;
    public Vector2 scale;
    public float rotation;

    public TransformComponent() {
        this.position = new Vector3();
        this.scale = new Vector2(1.0f, 1.0f);
    }

    public TransformComponent(float x, float y) {
        this.position = new Vector3(x, y, 0);
        this.scale = new Vector2(1.0f, 1.0f);
    }

    public TransformComponent(Vector3 position) {
        this.position = position;
        this.scale = new Vector2(1.0f, 1.0f);
    }

    public TransformComponent(Vector3 position, Vector2 scale) {
        this.position = position;
        this.scale = scale;
    }

    public TransformComponent(float x, float y, float scale) {
        this.position = new Vector3(x, y, 0f);
        this.scale = new Vector2(scale, scale);
    }
}
