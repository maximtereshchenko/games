package com.github.maximtereshchenko.games.snake;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class IncrementLaunchesTest {

    private final UserProfile userProfile = mock();
    private final IncrementLaunches incrementLaunches = new IncrementLaunches(userProfile);

    @Test
    void givenTitleScreenFinished_thenLaunchesIncremented() {
        when(userProfile.value(UserProfileStatistics.LAUNCHES)).thenReturn(1);
        incrementLaunches.onEvent(new TitleScreenFinished());
        verify(userProfile).update(UserProfileStatistics.LAUNCHES, 2);
    }
}