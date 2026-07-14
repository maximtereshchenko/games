package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Preferences;

final class UserProfile {

    private final Preferences preferences;

    UserProfile(Preferences preferences) {
        this.preferences = preferences;
    }

    boolean isUnlocked(Mode mode) {
        if (mode == Mode.CLASSIC) {
            return true;
        }
        return preferences.getBoolean(mode.toString());
    }

    void unlock(Mode mode) {
        preferences.putBoolean(mode.toString(), true);
    }

    void save() {
        preferences.flush();
    }
}
