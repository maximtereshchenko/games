package com.github.maximtereshchenko.games.snake;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

final class ModeUnlocksTest {

    private final UserProfile userProfile = mock();
    private final ModeUnlockRequirements modeUnlockRequirements = mock();
    private final Mode mode = mock();
    private final ModeUnlocks modeUnlocks = new ModeUnlocks(
        userProfile,
        List.of(mode)
    );

    @Test
    void givenSnakeSessionEnded_thenModeUnlocked() {
        var snakeSessionEnded = new SnakeSessionEnded(Map.of());
        when(mode.modeUnlockRequirements()).thenReturn(modeUnlockRequirements);
        when(modeUnlockRequirements.isSatisfied(snakeSessionEnded)).thenReturn(true);
        modeUnlocks.onEvent(snakeSessionEnded);
        verify(userProfile).unlock(mode);
    }

    @Test
    void givenTitleScreenFinished_thenModeUnlocked() {
        when(mode.modeUnlockRequirements()).thenReturn(modeUnlockRequirements);
        when(modeUnlockRequirements.isSatisfied()).thenReturn(true);
        modeUnlocks.onEvent(new TitleScreenFinished());
        verify(userProfile).unlock(mode);
    }
}