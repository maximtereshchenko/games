package com.github.maximtereshchenko.snakes;

import com.github.maximtereshchenko.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.snakes.event.CreditsScreenFinished;
import com.github.maximtereshchenko.snakes.event.Subscriber;
import com.github.maximtereshchenko.snakes.event.TitleScreenFinished;

final class IncrementUserProfileMetrics implements Subscriber {

    private final UserProfile userProfile;

    IncrementUserProfileMetrics(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        switch (applicationEvent) {
            case CreditsScreenFinished _ -> userProfile.increment(UserProfileMetric.CREDITS_READ);
            case TitleScreenFinished _ -> userProfile.increment(UserProfileMetric.LAUNCHES);
            default -> {
                //empty
            }
        }
    }
}
