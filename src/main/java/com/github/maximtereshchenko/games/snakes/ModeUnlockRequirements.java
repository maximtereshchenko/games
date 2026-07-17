package com.github.maximtereshchenko.games.snakes;

import com.github.maximtereshchenko.games.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.games.snakes.session.SessionStatistics;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

final class ModeUnlockRequirements {

    private final Map<UserProfileStatistics, Integer> userProfileThresholds;
    private final Map<SessionStatistics, Integer> sessionThresholds;

    ModeUnlockRequirements(
        Map<UserProfileStatistics, Integer> userProfileThresholds,
        Map<SessionStatistics, Integer> sessionThresholds
    ) {
        this.userProfileThresholds = userProfileThresholds;
        this.sessionThresholds = sessionThresholds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileThresholds, sessionThresholds);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof ModeUnlockRequirements that &&
               Objects.equals(userProfileThresholds, that.userProfileThresholds) &&
               Objects.equals(sessionThresholds, that.sessionThresholds);
    }

    boolean isSatisfied(UserProfile userProfile) {
        return isSatisfied(userProfile, entry -> entry.getValue() == 0);
    }

    boolean isSatisfied(UserProfile userProfile, SnakeSessionEnded snakeSessionEnded) {
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
