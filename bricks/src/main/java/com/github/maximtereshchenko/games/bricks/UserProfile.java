package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.Preferences;

public final class UserProfile {

    private static final String UNLOCKED = "levels.%s.%d.unlocked";
    private static final String STARS = "levels.%s.%d.stars";

    private final Preferences preferences;

    public UserProfile(Preferences preferences) {
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

    void save() {
        preferences.flush();
    }
}
