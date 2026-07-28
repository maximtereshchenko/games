package com.github.maximtereshchenko.snakes;

import com.github.maximtereshchenko.snakes.configuration.Mode;
import com.github.maximtereshchenko.snakes.configuration.ModeUnlockRequirements;
import com.github.maximtereshchenko.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.snakes.event.CreditsScreenFinished;
import com.github.maximtereshchenko.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.snakes.event.TitleScreenFinished;
import com.github.maximtereshchenko.snakes.session.SessionMetric;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

final class UnlockModesTest {

    private final UserProfile userProfile = mock();
    private final Mode mode = mock();
    private final Map<UserProfileMetric, Integer> userProfileThresholds =
        new EnumMap<>(UserProfileMetric.class);
    private final Map<SessionMetric, Integer> sessionThresholds =
        new EnumMap<>(SessionMetric.class);
    private final ModeUnlockRequirements modeUnlockRequirements = new ModeUnlockRequirements(
        userProfileThresholds,
        sessionThresholds
    );
    private final UnlockModes unlockModes = new UnlockModes(userProfile, List.of(mode));

    private static Stream<ApplicationEvent> events() {
        return Stream.of(
            new TitleScreenFinished(),
            new CreditsScreenFinished()
        );
    }

    @BeforeEach
    void setUp() {
        when(mode.modeUnlockRequirements()).thenReturn(modeUnlockRequirements);
    }

    @ParameterizedTest
    @MethodSource("events")
    void givenUserProfileThresholdsLesser_whenScreenFinished_thenModeUnlocked(
        ApplicationEvent event
    ) {
        userProfileThresholds.put(UserProfileMetric.LAUNCHES, 1);
        when(userProfile.value(UserProfileMetric.LAUNCHES)).thenReturn(2);
        unlockModes.onEvent(event);
        verify(userProfile).unlock(mode);
    }

    @ParameterizedTest
    @MethodSource("events")
    void givenUserProfileThresholdsGreater_whenScreenFinished_thenModeNotUnlocked(
        ApplicationEvent event
    ) {
        userProfileThresholds.put(UserProfileMetric.LAUNCHES, 1);
        unlockModes.onEvent(event);
        verify(userProfile, never()).unlock(mode);
    }

    @ParameterizedTest
    @MethodSource("events")
    void givenSessionThresholdsNotZero_whenScreenFinished_thenModeNotUnlocked(
        ApplicationEvent event
    ) {
        userProfileThresholds.put(UserProfileMetric.LAUNCHES, 1);
        sessionThresholds.put(SessionMetric.LEFT_TURNS, 1);
        when(userProfile.value(UserProfileMetric.LAUNCHES)).thenReturn(1);
        unlockModes.onEvent(event);
        verify(userProfile, never()).unlock(mode);
    }

    @Test
    void givenUserProfileThresholdsGreater_whenSnakeSessionEnded_thenModeNotUnlocked() {
        userProfileThresholds.put(UserProfileMetric.LAUNCHES, 1);
        unlockModes.onEvent(new SnakeSessionEnded(Map.of()));
        verify(userProfile, never()).unlock(mode);
    }

    @Test
    void givenSessionThresholdsGreater_whenSnakeSessionEnded_thenModeNotUnlocked() {
        userProfileThresholds.put(UserProfileMetric.LAUNCHES, 1);
        sessionThresholds.put(SessionMetric.LEFT_TURNS, 1);
        when(userProfile.value(UserProfileMetric.LAUNCHES)).thenReturn(2);
        unlockModes.onEvent(new SnakeSessionEnded(Map.of(SessionMetric.LEFT_TURNS, 0)));
        verify(userProfile, never()).unlock(mode);
    }

    @Test
    void givenSessionThresholdsLesser_whenSnakeSessionEnded_thenModeUnlocked() {
        userProfileThresholds.put(UserProfileMetric.LAUNCHES, 1);
        sessionThresholds.put(SessionMetric.LEFT_TURNS, 1);
        when(userProfile.value(UserProfileMetric.LAUNCHES)).thenReturn(2);
        unlockModes.onEvent(new SnakeSessionEnded(Map.of(SessionMetric.LEFT_TURNS, 2)));
        verify(userProfile).unlock(mode);
    }
}
