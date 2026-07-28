package com.github.maximtereshchenko.snakes;

import com.badlogic.gdx.Preferences;
import com.github.maximtereshchenko.snakes.configuration.Configuration;
import com.github.maximtereshchenko.snakes.configuration.Mode;

public final class UserProfile {

    private static final String MUSIC_VOLUME_KEY = "music.volume";

    private final Configuration configuration;
    private final Preferences preferences;

    UserProfile(Configuration configuration, Preferences preferences) {
        this.configuration = configuration;
        this.preferences = preferences;
    }

    public boolean isUnlocked(Mode mode) {
        return preferences.getBoolean(mode.name());
    }

    public int value(UserProfileMetric userProfileMetric) {
        return preferences.getInteger(userProfileMetric.name());
    }

    public void updateMusicVolume(float volume) {
        preferences.putFloat(MUSIC_VOLUME_KEY, volume);
    }

    void unlock(Mode mode) {
        preferences.putBoolean(mode.name(), true);
    }

    void increment(UserProfileMetric userProfileMetric) {
        preferences.putInteger(userProfileMetric.name(), value(userProfileMetric) + 1);
    }

    float musicVolume() {
        return preferences.getFloat(MUSIC_VOLUME_KEY, configuration.defaultMusicVolume());
    }

    void save() {
        preferences.flush();
    }
}
