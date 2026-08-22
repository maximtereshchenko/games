package com.github.maximtereshchenko.games.bricks.event;

import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.event.Subscriber;

public final class UpdateStars implements Subscriber<Event> {

    private final UserProfile userProfile;

    public UpdateStars(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof LevelCompleted levelCompleted) {
            userProfile.updateStars(
                levelCompleted.difficulty(),
                levelCompleted.level(),
                levelCompleted.stars()
            );
        }
    }
}
