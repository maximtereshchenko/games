package com.github.maximtereshchenko.games.snakes.configuration;

import com.github.maximtereshchenko.games.snakes.UserProfileMetric;
import com.github.maximtereshchenko.games.snakes.session.SessionMetric;

import java.util.Map;
import java.util.Objects;

public record ModeUnlockRequirements(
    Map<UserProfileMetric, Integer> userProfileThresholds,
    Map<SessionMetric, Integer> sessionThresholds
) {

    public ModeUnlockRequirements {
        userProfileThresholds = Objects.requireNonNullElse(userProfileThresholds, Map.of());
        sessionThresholds = Objects.requireNonNullElse(sessionThresholds, Map.of());
    }
}
