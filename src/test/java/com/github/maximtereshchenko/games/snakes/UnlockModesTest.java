package com.github.maximtereshchenko.games.snakes;

import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import com.github.maximtereshchenko.games.snakes.configuration.ModeUnlockRequirements;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.games.snakes.event.CreditsScreenFinished;
import com.github.maximtereshchenko.games.snakes.event.SnakeSessionEnded;
import com.github.maximtereshchenko.games.snakes.event.TitleScreenFinished;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

final class UnlockModesTest {

    private final UserProfile userProfile = mock();
    private final ModeUnlockRequirements modeUnlockRequirements = mock();
    private final Mode mode = mock();
    private final UnlockModes unlockModes = new UnlockModes(
        userProfile,
        List.of(mode)
    );

    private static Stream<ApplicationEvent> events() {
        return Stream.of(
            new TitleScreenFinished(),
            new CreditsScreenFinished()
        );
    }

    @Test
    void givenSnakeSessionEnded_thenModeUnlocked() {
        var snakeSessionEnded = new SnakeSessionEnded(Map.of());
        when(mode.modeUnlockRequirements()).thenReturn(modeUnlockRequirements);
        when(modeUnlockRequirements.isSatisfied(userProfile, snakeSessionEnded))
            .thenReturn(true);
        unlockModes.onEvent(snakeSessionEnded);
        verify(userProfile).unlock(mode);
    }

    @ParameterizedTest
    @MethodSource("events")
    void givenTitleScreenFinished_thenModeUnlockedBasedOnProfile() {
        when(mode.modeUnlockRequirements()).thenReturn(modeUnlockRequirements);
        when(modeUnlockRequirements.isSatisfied(userProfile))
            .thenReturn(true);
        unlockModes.onEvent(new TitleScreenFinished());
        verify(userProfile).unlock(mode);
    }
}