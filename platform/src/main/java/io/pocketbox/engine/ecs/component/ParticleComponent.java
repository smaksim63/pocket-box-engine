package io.pocketbox.engine.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleComponent implements Component {
    public final ParticleEffect particleEffect;

    public ParticleComponent(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
    }
}
