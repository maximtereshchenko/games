package com.github.maximtereshchenko.games.snakes;

import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import com.github.maximtereshchenko.games.snakes.event.*;
import com.github.maximtereshchenko.games.snakes.session.SessionMetric;

import java.util.List;
import java.util.Map;

final class UpdateUserProfileMetrics implements Subscriber<ApplicationEvent> {

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
            case SessionEnded sessionEnded -> {
                update(sessionEnded.statistics());
                updateModesUnlocked();
            }
            default -> {
                //empty
            }
        }
    }

    private void update(Map<SessionMetric, Integer> statistics) {
        var foodConsumed = statistics.get(SessionMetric.FOOD_CONSUMED);
        if (foodConsumed == 1) {
            userProfile.increment(UserProfileMetric.ONE_FOOD_CONSUMED);
        }
        if (foodConsumed % 2 != 0) {
            userProfile.increment(UserProfileMetric.ODD_FOOD_CONSUMED);
        }
        userProfile.update(
            UserProfileMetric.FOOD_CONSUMED,
            userProfile.value(UserProfileMetric.FOOD_CONSUMED) +
            statistics.get(SessionMetric.FOOD_CONSUMED)
        );
        userProfile.update(
            UserProfileMetric.WARPS,
            userProfile.value(UserProfileMetric.WARPS) +
            statistics.get(SessionMetric.WARPS)
        );
        userProfile.increment(UserProfileMetric.FINISHED_SESSIONS);
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
