package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.Preferences;

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

    public int value(UserProfileStatistics statistics) {
        return preferences.getInteger(statistics.name());
    }

    public void updateMusicVolume(float volume) {
        preferences.putFloat(MUSIC_VOLUME_KEY, volume);
    }

    void unlock(Mode mode) {
        preferences.putBoolean(mode.name(), true);
    }

    void update(UserProfileStatistics statistics, int value) {
        preferences.putInteger(statistics.name(), value);
    }

    void increment(UserProfileStatistics statistics) {
        update(statistics, value(statistics) + 1);
    }

    float musicVolume() {
        return preferences.getFloat(MUSIC_VOLUME_KEY, configuration.defaultMusicVolume());
    }

    void save() {
        preferences.flush();
    }
}
