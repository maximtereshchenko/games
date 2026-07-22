package com.github.maximtereshchenko.games.snakes.configuration;

import com.github.maximtereshchenko.games.snakes.UserProfile;
import com.github.maximtereshchenko.games.snakes.UserProfileStatistics;
import com.github.maximtereshchenko.games.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.games.snakes.session.SessionStatistics;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class ModeUnlockRequirementsTest {

    private final UserProfile userProfile = mock();
    private final Map<UserProfileStatistics, Integer> userProfileThresholds =
        new EnumMap<>(UserProfileStatistics.class);
    private final Map<SessionStatistics, Integer> sessionThresholds =
        new EnumMap<>(SessionStatistics.class);
    private final ModeUnlockRequirements modeUnlockRequirements = new ModeUnlockRequirements(
        userProfileThresholds,
        sessionThresholds
    );

    @Test
    void givenUserProfileThresholdsGreater_whenIsSatisfied_thenFalse() {
        userProfileThresholds.put(UserProfileStatistics.LAUNCHES, 1);
        assertThat(modeUnlockRequirements.isSatisfied(userProfile)).isFalse();
    }

    @Test
    void givenSessionThresholdsNotZero_whenIsSatisfied_thenFalse() {
        userProfileThresholds.put(UserProfileStatistics.LAUNCHES, 1);
        sessionThresholds.put(SessionStatistics.LEFT_TURNS, 1);
        when(userProfile.value(UserProfileStatistics.LAUNCHES)).thenReturn(1);
        assertThat(modeUnlockRequirements.isSatisfied(userProfile)).isFalse();
    }

    @Test
    void givenUserProfileThresholdsLesser_whenIsSatisfied_thenTrue() {
        userProfileThresholds.put(UserProfileStatistics.LAUNCHES, 1);
        when(userProfile.value(UserProfileStatistics.LAUNCHES)).thenReturn(2);
        assertThat(modeUnlockRequirements.isSatisfied(userProfile)).isTrue();
    }

    @Test
    void givenUserProfileThresholdsGreater_whenIsSatisfiedWithSnakeSessionEnded_thenFalse() {
        userProfileThresholds.put(UserProfileStatistics.LAUNCHES, 1);
        assertThat(
            modeUnlockRequirements.isSatisfied(
                userProfile,
                new SnakeSessionEnded(Map.of())
            )
        )
            .isFalse();
    }

    @Test
    void givenSessionThresholdsGreater_whenIsSatisfiedWithSnakeSessionEnded_thenFalse() {
        userProfileThresholds.put(UserProfileStatistics.LAUNCHES, 1);
        sessionThresholds.put(SessionStatistics.LEFT_TURNS, 1);
        when(userProfile.value(UserProfileStatistics.LAUNCHES)).thenReturn(2);
        assertThat(
            modeUnlockRequirements.isSatisfied(
                userProfile,
                new SnakeSessionEnded(Map.of(SessionStatistics.LEFT_TURNS, 0))
            )
        )
            .isFalse();
    }

    @Test
    void givenSessionThresholdsLesser_whenIsSatisfiedWithSnakeSessionEnded_thenTrue() {
        userProfileThresholds.put(UserProfileStatistics.LAUNCHES, 1);
        sessionThresholds.put(SessionStatistics.LEFT_TURNS, 1);
        when(userProfile.value(UserProfileStatistics.LAUNCHES)).thenReturn(2);
        assertThat(
            modeUnlockRequirements.isSatisfied(
                userProfile,
                new SnakeSessionEnded(Map.of(SessionStatistics.LEFT_TURNS, 2))
            )
        )
            .isTrue();
    }
}