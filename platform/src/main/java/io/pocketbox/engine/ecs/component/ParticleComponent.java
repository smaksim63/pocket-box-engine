package io.pocketbox.engine.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import static io.pocketbox.engine.Config.PIXEL_TO_METER;

public class ParticleComponent implements Component {
    public final ParticleEffect particleEffect;

    public ParticleComponent(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
        this.particleEffect.scaleEffect(PIXEL_TO_METER);
    }
}
