package com.github.maximtereshchenko.snakes;

import com.github.maximtereshchenko.snakes.configuration.Mode;
import com.github.maximtereshchenko.snakes.event.CreditsScreenFinished;
import com.github.maximtereshchenko.snakes.event.SessionEnded;
import com.github.maximtereshchenko.snakes.event.TitleScreenFinished;
import com.github.maximtereshchenko.snakes.session.SessionMetric;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

final class UpdateUserProfileMetricsTest {

    private final UserProfile userProfile = mock();
    private final Mode mode = mock();
    private final UpdateUserProfileMetrics updateUserProfileMetrics =
        new UpdateUserProfileMetrics(userProfile, List.of(mode));

    @Test
    void givenTitleScreenFinished_thenLaunchesIncrementedModesUnlockedUpdated() {
        updateUserProfileMetrics.onEvent(new TitleScreenFinished());
        verify(userProfile).increment(UserProfileMetric.LAUNCHES);
        verify(userProfile).isUnlocked(mode);
        verify(userProfile).update(UserProfileMetric.MODES_UNLOCKED, 0);
    }

    @Test
    void givenCreditsScreenFinished_thenCreditsReadIncrementedModesUnlockedUpdated() {
        updateUserProfileMetrics.onEvent(new CreditsScreenFinished());
        verify(userProfile).increment(UserProfileMetric.CREDITS_READ);
        verify(userProfile).isUnlocked(mode);
        verify(userProfile).update(UserProfileMetric.MODES_UNLOCKED, 0);
    }

    @Test
    void givenSessionEnded_thenStatisticsAppliedModesUnlockedUpdated() {
        when(userProfile.isUnlocked(mode)).thenReturn(true);
        when(userProfile.value(UserProfileMetric.FOOD_CONSUMED)).thenReturn(4);
        when(userProfile.value(UserProfileMetric.WARPS)).thenReturn(2);
        updateUserProfileMetrics.onEvent(
            new SessionEnded(
                Map.of(
                    SessionMetric.FOOD_CONSUMED, 2,
                    SessionMetric.WARPS, 3
                )
            )
        );
        verify(userProfile, never()).increment(UserProfileMetric.ONE_FOOD_CONSUMED);
        verify(userProfile, never()).increment(UserProfileMetric.ODD_FOOD_CONSUMED);
        verify(userProfile).update(UserProfileMetric.FOOD_CONSUMED, 6);
        verify(userProfile).update(UserProfileMetric.WARPS, 5);
        verify(userProfile).increment(UserProfileMetric.FINISHED_SESSIONS);
        verify(userProfile).isUnlocked(mode);
        verify(userProfile).update(UserProfileMetric.MODES_UNLOCKED, 1);
    }

    @Test
    void givenSessionEndedWithOneFoodConsumed_thenOneFoodAndOddFoodConsumedIncremented() {
        when(userProfile.value(UserProfileMetric.FOOD_CONSUMED)).thenReturn(0);
        when(userProfile.value(UserProfileMetric.WARPS)).thenReturn(0);
        updateUserProfileMetrics.onEvent(
            new SessionEnded(
                Map.of(
                    SessionMetric.FOOD_CONSUMED, 1,
                    SessionMetric.WARPS, 0
                )
            )
        );
        verify(userProfile).increment(UserProfileMetric.ONE_FOOD_CONSUMED);
        verify(userProfile).increment(UserProfileMetric.ODD_FOOD_CONSUMED);
        verify(userProfile).update(UserProfileMetric.FOOD_CONSUMED, 1);
        verify(userProfile).update(UserProfileMetric.WARPS, 0);
        verify(userProfile).increment(UserProfileMetric.FINISHED_SESSIONS);
    }

    @Test
    void givenSessionEndedWithOddFoodConsumed_thenOddFoodConsumedIncremented() {
        when(userProfile.value(UserProfileMetric.FOOD_CONSUMED)).thenReturn(0);
        when(userProfile.value(UserProfileMetric.WARPS)).thenReturn(0);
        updateUserProfileMetrics.onEvent(
            new SessionEnded(
                Map.of(
                    SessionMetric.FOOD_CONSUMED, 3,
                    SessionMetric.WARPS, 0
                )
            )
        );
        verify(userProfile, never()).increment(UserProfileMetric.ONE_FOOD_CONSUMED);
        verify(userProfile).increment(UserProfileMetric.ODD_FOOD_CONSUMED);
        verify(userProfile).update(UserProfileMetric.FOOD_CONSUMED, 3);
        verify(userProfile).update(UserProfileMetric.WARPS, 0);
        verify(userProfile).increment(UserProfileMetric.FINISHED_SESSIONS);
    }
}
