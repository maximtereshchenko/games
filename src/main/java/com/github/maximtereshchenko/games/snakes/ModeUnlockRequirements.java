package com.github.maximtereshchenko.games.snakes;

import com.github.maximtereshchenko.games.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.games.snakes.session.SessionStatistics;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

final class ModeUnlockRequirements {

    private final UserProfile userProfile;
    private final Map<UserProfileStatistics, Integer> userProfileThresholds;
    private final Map<SessionStatistics, Integer> sessionThresholds;

    ModeUnlockRequirements(
        UserProfile userProfile,
        Map<UserProfileStatistics, Integer> userProfileThresholds,
        Map<SessionStatistics, Integer> sessionThresholds
    ) {
        this.userProfile = userProfile;
        this.userProfileThresholds = userProfileThresholds;
        this.sessionThresholds = sessionThresholds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfile, userProfileThresholds, sessionThresholds);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof ModeUnlockRequirements that &&
               Objects.equals(userProfile, that.userProfile) &&
               Objects.equals(userProfileThresholds, that.userProfileThresholds) &&
               Objects.equals(sessionThresholds, that.sessionThresholds);
    }

    boolean isSatisfied() {
        return isSatisfied(entry -> entry.getValue() == 0);
    }

    boolean isSatisfied(SnakeSessionEnded snakeSessionEnded) {
        return isSatisfied(entry -> snakeSessionEnded.statistics().get(entry.getKey()) >= entry.getValue());
    }

    private boolean isSatisfied(Predicate<Map.Entry<SessionStatistics, Integer>> predicate) {
        return userProfileThresholds.entrySet()
                   .stream()
                   .allMatch(entry -> userProfile.value(entry.getKey()) >= entry.getValue()) &&
               sessionThresholds.entrySet()
                   .stream()
                   .allMatch(predicate);
    }
}
