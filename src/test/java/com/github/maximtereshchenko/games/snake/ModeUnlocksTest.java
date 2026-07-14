package com.github.maximtereshchenko.games.snake;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Predicate;

import static org.mockito.Mockito.*;

final class ModeUnlocksTest {

    private final UserProfile userProfile = mock();
    private final Predicate<SnakeSessionEnded> predicate = mock();
    private final ModeUnlocks modeUnlocks = new ModeUnlocks(
        userProfile,
        Map.of(Mode.VIPER, predicate)
    );

    @Test
    void givenNoSnakeSessionEnded_thenNoChanges() {
        modeUnlocks.onEvent(new AssetsLoaded());
        verifyNoInteractions(userProfile);
    }

    @Test
    void givenPredicateFalse_thenNoChanges() {
        modeUnlocks.onEvent(new SnakeSessionEnded(0));
        verifyNoInteractions(userProfile);
    }

    @Test
    void givenPredicateTrue_thenModeUnlocked() {
        var snakeSessionEnded = new SnakeSessionEnded(0);
        when(predicate.test(snakeSessionEnded)).thenReturn(true);
        modeUnlocks.onEvent(snakeSessionEnded);
        verify(userProfile).unlock(Mode.VIPER);
    }
}