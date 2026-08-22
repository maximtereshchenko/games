package com.github.maximtereshchenko.games.bricks.event;

import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.event.Subscriber;

public final class UnlockFirstLevels implements Subscriber<Event> {

    private final Configuration configuration;
    private final UserProfile userProfile;

    public UnlockFirstLevels(
        Configuration configuration,
        UserProfile userProfile
    ) {
        this.configuration = configuration;
        this.userProfile = userProfile;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof AssetsLoaded) {
            for (var difficulty : configuration.difficulties().keySet()) {
                userProfile.unlock(difficulty, 0);
            }
        }
    }
}
