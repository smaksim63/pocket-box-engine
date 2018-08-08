package io.pocketbox.engine.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class RigidBodyComponent implements Component {
    public final BoxCollider boxCollider;
    public Body body;
    public Vector2 offset;

    public RigidBodyComponent(BoxCollider boxCollider) {
        this.boxCollider = boxCollider;
        this.offset = new Vector2();
    }

    public RigidBodyComponent(BoxCollider boxCollider, float x, float y) {
        this(boxCollider);
        this.offset.set(x, y);
    }

}
