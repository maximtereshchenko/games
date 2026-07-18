package com.github.maximtereshchenko.games.snakes;

import com.github.maximtereshchenko.games.snakes.event.CreditsScreenFinished;
import com.github.maximtereshchenko.games.snakes.event.TitleScreenFinished;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

final class IncrementStatisticsTest {

    private final UserProfile userProfile = mock();
    private final IncrementStatistics incrementStatistics =
        new IncrementStatistics(userProfile);

    @Test
    void givenTitleScreenFinished_thenLaunchesIncremented() {
        incrementStatistics.onEvent(new TitleScreenFinished());
        verify(userProfile).increment(UserProfileStatistics.LAUNCHES);
    }

    @Test
    void givenCreditsScreenFinished_thenCreditsReadIncremented() {
        incrementStatistics.onEvent(new CreditsScreenFinished());
        verify(userProfile).increment(UserProfileStatistics.CREDITS_READ);
    }
}