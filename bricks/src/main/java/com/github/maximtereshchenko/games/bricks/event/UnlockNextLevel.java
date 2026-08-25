package com.github.maximtereshchenko.games.bricks.event;

import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.common.event.Subscriber;

public final class UnlockNextLevel implements Subscriber<Event> {

    private final UserProfile userProfile;

    public UnlockNextLevel(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof LevelCompleted levelCompleted) {
            userProfile.unlock(
                levelCompleted.difficulty(),
                levelCompleted.level() + 1
            );
        }
    }
}
