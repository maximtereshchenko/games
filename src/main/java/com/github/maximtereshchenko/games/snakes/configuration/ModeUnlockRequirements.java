package com.github.maximtereshchenko.games.snakes.configuration;

import com.github.maximtereshchenko.games.snakes.UserProfile;
import com.github.maximtereshchenko.games.snakes.UserProfileStatistics;
import com.github.maximtereshchenko.games.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.games.snakes.session.SessionStatistics;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public record ModeUnlockRequirements(
    Map<UserProfileStatistics, Integer> userProfileThresholds,
    Map<SessionStatistics, Integer> sessionThresholds
) {

    public ModeUnlockRequirements {
        userProfileThresholds = Objects.requireNonNullElse(userProfileThresholds, Map.of());
        sessionThresholds = Objects.requireNonNullElse(sessionThresholds, Map.of());
    }

    public boolean isSatisfied(UserProfile userProfile) {
        return isSatisfied(userProfile, entry -> entry.getValue() == 0); //TODO empty
    }

    public boolean isSatisfied(UserProfile userProfile, SnakeSessionEnded snakeSessionEnded) {
        return isSatisfied(
            userProfile,
            entry -> snakeSessionEnded.statistics().get(entry.getKey()) >= entry.getValue()
        );
    }

    private boolean isSatisfied(
        UserProfile userProfile,
        Predicate<Map.Entry<SessionStatistics, Integer>> predicate
    ) {
        return userProfileThresholds.entrySet()
                   .stream()
                   .allMatch(entry -> userProfile.value(entry.getKey()) >= entry.getValue()) &&
               sessionThresholds.entrySet()
                   .stream()
                   .allMatch(predicate);
    }
}
