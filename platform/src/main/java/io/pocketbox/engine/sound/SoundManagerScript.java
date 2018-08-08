package io.pocketbox.engine.sound;

import com.badlogic.gdx.audio.Sound;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.event.EventBus;
import io.pocketbox.engine.scripting.Script;

public class SoundManagerScript extends Script<SoundEvent> {

    private final GameContext gameContext;
    private Sound currentSound;
    private long soundId;
    private float volume;

    private final String SOUND_LEVEL_PREF = "SOUND_LEVEL_PREF";

    public SoundManagerScript(GameContext gameContext) {
        this.gameContext = gameContext;
        volume = gameContext.preferences.getFloat(SOUND_LEVEL_PREF, 1.0f);
    }

    private void saveVolume() {
        gameContext.preferences.putFloat(SOUND_LEVEL_PREF, volume);
        gameContext.preferences.flush();
    }

    @Override
    public void onMessage(SoundEvent event) {
        currentSound = gameContext.assetsSource.getAsset(event.soundName, Sound.class);
        soundId = currentSound.play(volume);
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (currentSound != null) {
            currentSound.setVolume(soundId, volume);
        }
        saveVolume();
    }

    public void mute() {
        if (volume <= 0f) {
            setVolume(1f);
        } else {
            setVolume(0f);
        }
    }

    public boolean isMute() {
        return volume <= 0f;
    }

    @Override
    public void onStart() {
        EventBus.instance().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.instance().unregister(this);
    }
}
