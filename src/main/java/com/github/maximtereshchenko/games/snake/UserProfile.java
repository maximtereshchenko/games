package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Preferences;

final class UserProfile {

    private final Preferences preferences;

    UserProfile(Preferences preferences) {
        this.preferences = preferences;
    }

    boolean isUnlocked(Mode mode) {
        return preferences.getBoolean(mode.name());
    }

    void unlock(Mode mode) {
        preferences.putBoolean(mode.name(), true);
    }

    int value(UserProfileStatistics statistics) {
        return preferences.getInteger(statistics.name());
    }

    void update(UserProfileStatistics statistics, int value) {
        preferences.putInteger(statistics.name(), value);
    }

    void save() {
        preferences.flush();
    }
}
