package io.pocketbox.engine.ecs.component;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class BoxCollider {

    private final PolygonShape shape;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    public BoxCollider() {
        this.shape = new PolygonShape();
        this.bodyDef = new BodyDef();
        this.fixtureDef = new FixtureDef();
        this.fixtureDef.shape = shape;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public BoxCollider setType(BodyType bodyType) {
        this.bodyDef.type = bodyType;
        return this;
    }

    public BoxCollider setDensity(float value) {
        this.fixtureDef.density = value;
        return this;
    }

    public BoxCollider setFriction(float value) {
        this.fixtureDef.friction = value;
        return this;
    }

    public BoxCollider setRestitution(float value) {
        this.fixtureDef.restitution = value;
        return this;
    }

    public BoxCollider setAsABox(float w, float h) {
        this.shape.setAsBox(w * 0.5f,
                h * 0.5f);
        return this;
    }
}
