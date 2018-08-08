package io.pocketbox.prototype.scripts;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.scripting.Script;

import static io.pocketbox.engine.DynamicConfig.*;

public class MovementCameraScript extends Script {

    private final Camera camera;
    private TransformComponent transformComponent;

    public MovementCameraScript(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void onStart() {
        transformComponent = gameObject.getComponent(TransformComponent.class);
    }

    @Override
    public void update() {
        float xPos = transformComponent.position.x;
        float yPos = transformComponent.position.y;

        float halfWidth = WORLD_WIDTH * 0.5f;
        float halfHeight = WORLD_HEIGHT * 0.5f;

        camera.position.x = MathUtils.clamp(xPos, -9.6f + halfWidth, 9.6f - halfWidth);
        camera.position.y = MathUtils.clamp(yPos, -5.4f + halfHeight, 5.4f - halfHeight);
    }
}
