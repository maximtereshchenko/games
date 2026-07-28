package com.github.maximtereshchenko.snakes.configuration;

import com.github.maximtereshchenko.snakes.UserProfileMetric;
import com.github.maximtereshchenko.snakes.session.SessionMetric;

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
