package io.pocketbox.engine.sound;

import com.badlogic.gdx.audio.Music;
import io.pocketbox.engine.GameContext;
import io.pocketbox.engine.event.EventBus;
import io.pocketbox.engine.scripting.Script;

public class MusicManagerScript extends Script<MusicEvent> {

    private GameContext gameContext;
    private Music currentMusic;
    private float volume;

    private final String MUSIC_LEVEL_PREF = "MUSIC_LEVEL_PREF";

    public MusicManagerScript(GameContext gameContext) {
        this.gameContext = gameContext;
        volume = gameContext.preferences.getFloat(MUSIC_LEVEL_PREF, 1.0f);
    }

    private void saveVolume() {
        gameContext.preferences.putFloat(MUSIC_LEVEL_PREF, volume);
        gameContext.preferences.flush();
    }

    @Override
    public void onMessage(MusicEvent event) {
        Music music = gameContext.assetsSource.getAsset(event.musicName, Music.class);
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
        currentMusic = music;
        currentMusic.setVolume(volume);
        music.play();
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
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
