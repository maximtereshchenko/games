package com.github.maximtereshchenko.snakes;

import com.github.maximtereshchenko.snakes.configuration.Mode;
import com.github.maximtereshchenko.snakes.event.*;

import java.util.List;

final class UpdateUserProfileMetrics implements Subscriber {

    private final UserProfile userProfile;
    private final List<Mode> modes;

    UpdateUserProfileMetrics(UserProfile userProfile, List<Mode> modes) {
        this.userProfile = userProfile;
        this.modes = modes;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        switch (applicationEvent) {
            case CreditsScreenFinished _ -> {
                userProfile.increment(UserProfileMetric.CREDITS_READ);
                updateModesUnlocked();
            }
            case TitleScreenFinished _ -> {
                userProfile.increment(UserProfileMetric.LAUNCHES);
                updateModesUnlocked();
            }
            case SessionEnded _ -> updateModesUnlocked();
            default -> {
                //empty
            }
        }
    }

    private void updateModesUnlocked() {
        var modesUnlocked = 0;
        for (var mode : modes) {
            if (userProfile.isUnlocked(mode)) {
                modesUnlocked++;
            }
        }
        userProfile.update(UserProfileMetric.MODES_UNLOCKED, modesUnlocked);
    }
}
