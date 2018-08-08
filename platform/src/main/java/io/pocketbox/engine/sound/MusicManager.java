package io.pocketbox.engine.sound;

import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.scripting.GameObject;

public class MusicManager extends GameObject {

    private MusicManagerScript script;

    public MusicManager(GameContext gameContext) {
        super(gameContext);
        script = new MusicManagerScript(gameContext);
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
