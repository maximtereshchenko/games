package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.Preferences;

public final class UserProfile {

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

    void unlock(Mode mode) {
        preferences.putBoolean(mode.name(), true);
    }

    void update(UserProfileStatistics statistics, int value) {
        preferences.putInteger(statistics.name(), value);
    }

    float musicVolume() {
        return preferences.getFloat("music.volume", configuration.defaultMusicVolume());
    }

    void save() {
        preferences.flush();
    }
}
