package com.github.maximtereshchenko.games.snakes.configuration;

import com.github.maximtereshchenko.games.snakes.UserProfileStatistics;
import com.github.maximtereshchenko.games.snakes.session.SessionStatistics;

import java.util.Map;
import java.util.Objects;

public record ModeUnlockRequirements(
    Map<UserProfileStatistics, Integer> userProfileThresholds,
    Map<SessionStatistics, Integer> sessionThresholds
) {

    public ModeUnlockRequirements {
        userProfileThresholds = Objects.requireNonNullElse(userProfileThresholds, Map.of());
        sessionThresholds = Objects.requireNonNullElse(sessionThresholds, Map.of());
    }
}
