package io.pocketbox.prototype.objects;

import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.ecs.component.TextureComponent;
import io.pocketbox.engine.ecs.component.TransformComponent;
import io.pocketbox.engine.scripting.GameObject;

public class Background extends GameObject {
    public Background(GameContext gameContext,
                      String bgName) {
        super(gameContext);
        add(new TransformComponent());
        add(new TextureComponent(gameContext.assetsSource.getTextureRegion(bgName)));
        create();
    }
}
