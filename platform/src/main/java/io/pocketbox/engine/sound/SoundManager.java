package io.pocketbox.engine.sound;

import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.scripting.GameObject;

public class SoundManager extends GameObject {

    private SoundManagerScript script;

    public SoundManager(GameContext gameContext) {
        super(gameContext);
        script = new SoundManagerScript(gameContext);
        this.add(script);
        create();
    }

    public void setVolume(float volume) {
        script.setVolume(volume);
    }

    public void mute() {
        script.mute();
    }

    public boolean isMute() {
        return script.isMute();
    }
}
