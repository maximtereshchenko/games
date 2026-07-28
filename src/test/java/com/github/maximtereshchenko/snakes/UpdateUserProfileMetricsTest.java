package com.github.maximtereshchenko.snakes;

import com.github.maximtereshchenko.snakes.configuration.Mode;
import com.github.maximtereshchenko.snakes.event.CreditsScreenFinished;
import com.github.maximtereshchenko.snakes.event.SessionEnded;
import com.github.maximtereshchenko.snakes.event.TitleScreenFinished;
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
    void givenSessionEnded_thenModesUnlockedUpdated() {
        when(userProfile.isUnlocked(mode)).thenReturn(true);
        updateUserProfileMetrics.onEvent(new SessionEnded(Map.of()));
        verify(userProfile).isUnlocked(mode);
        verify(userProfile).update(UserProfileMetric.MODES_UNLOCKED, 1);
    }
}