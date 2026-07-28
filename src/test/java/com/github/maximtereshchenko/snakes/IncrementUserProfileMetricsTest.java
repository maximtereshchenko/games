package com.github.maximtereshchenko.snakes;

import com.github.maximtereshchenko.snakes.event.CreditsScreenFinished;
import com.github.maximtereshchenko.snakes.event.TitleScreenFinished;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class IncrementUserProfileMetricsTest {

    private final UserProfile userProfile = mock();
    private final IncrementUserProfileMetrics incrementUserProfileMetrics =
        new IncrementUserProfileMetrics(userProfile);

    @Test
    void givenTitleScreenFinished_thenLaunchesIncremented() {
        incrementUserProfileMetrics.onEvent(new TitleScreenFinished());
        verify(userProfile).increment(UserProfileMetric.LAUNCHES);
    }

    @Test
    void givenCreditsScreenFinished_thenCreditsReadIncremented() {
        incrementUserProfileMetrics.onEvent(new CreditsScreenFinished());
        verify(userProfile).increment(UserProfileMetric.CREDITS_READ);
    }
}