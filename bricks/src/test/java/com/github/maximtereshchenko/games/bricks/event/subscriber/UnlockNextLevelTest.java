package com.github.maximtereshchenko.games.bricks.event.subscriber;

import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.event.LevelCompleted;
import com.github.maximtereshchenko.games.bricks.event.LevelFailed;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class UnlockNextLevelTest {

    private final UserProfile userProfile = mock();
    private final UnlockNextLevel unlockNextLevel = new UnlockNextLevel(userProfile);

    @Test
    void givenLevelCompleted_thenNextLevelUnlocked() {
        unlockNextLevel.onEvent(new LevelCompleted("easy", 2, 3));
        verify(userProfile).unlock("easy", 3);
    }

    @Test
    void givenOtherEvent_thenLevelNotUnlocked() {
        unlockNextLevel.onEvent(new LevelFailed());
        verifyNoInteractions(userProfile);
    }
}
