package com.github.maximtereshchenko.games.snakes;

import com.github.maximtereshchenko.games.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.games.snakes.event.CreditsScreenFinished;
import com.github.maximtereshchenko.games.snakes.event.Subscriber;
import com.github.maximtereshchenko.games.snakes.event.TitleScreenFinished;

final class IncrementStatistics implements Subscriber {

    private final UserProfile userProfile;

    IncrementStatistics(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        switch (applicationEvent) {
            case CreditsScreenFinished _ -> userProfile.increment(UserProfileStatistics.CREDITS_READ);
            case TitleScreenFinished _ -> userProfile.increment(UserProfileStatistics.LAUNCHES);
            default -> {
                //empty
            }
        }
    }
}
