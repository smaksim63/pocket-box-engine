package io.pocketbox.engine.sound;

public class MusicEvent {
    public final String musicName;
    public final float volume;

    public MusicEvent(String musicName) {
        this.musicName = musicName;
        this.volume = 1.0f;
    }

    public MusicEvent(String musicName, float volume) {
        this.musicName = musicName;
        this.volume = volume;
    }
}
