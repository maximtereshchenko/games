package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.Preferences;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;

public final class UserProfile {

    private static final String UNLOCKED = "levels.%s.%d.unlocked";
    private static final String STARS = "levels.%s.%d.stars";
    private static final String MUSIC_VOLUME_KEY = "music.volume";
    private static final String SOUND_VOLUME_KEY = "sound.volume";

    private final Configuration configuration;
    private final Preferences preferences;

    public UserProfile(Configuration configuration, Preferences preferences) {
        this.configuration = configuration;
        this.preferences = preferences;
    }

    public boolean isUnlocked(String difficulty, int level) {
        return preferences.getBoolean(
            UNLOCKED.formatted(difficulty, level)
        );
    }

    public void unlock(String difficulty, int level) {
        preferences.putBoolean(
            UNLOCKED.formatted(difficulty, level),
            true
        );
    }

    public int stars(String difficulty, int level) {
        return preferences.getInteger(
            STARS.formatted(difficulty, level)
        );
    }

    public void updateStars(
        String difficulty,
        int level,
        int stars
    ) {
        preferences.putInteger(
            STARS.formatted(difficulty, level),
            stars
        );
    }

    public float musicVolume() {
        return preferences.getFloat(
            MUSIC_VOLUME_KEY,
            configuration.defaultMusicVolume()
        );
    }

    public void updateMusicVolume(float volume) {
        preferences.putFloat(MUSIC_VOLUME_KEY, volume);
    }

    public float soundVolume() {
        return preferences.getFloat(
            SOUND_VOLUME_KEY,
            configuration.defaultSoundVolume()
        );
    }

    public void updateSoundVolume(float volume) {
        preferences.putFloat(SOUND_VOLUME_KEY, volume);
    }

    void save() {
        preferences.flush();
    }
}
