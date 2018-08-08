package io.pocketbox.engine.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {
    public final TextureRegion texture;
    public TextureComponent(TextureRegion texture) {
        this.texture = texture;
    }
}
