package com.github.maximtereshchenko.games.bricks.event;

import com.github.maximtereshchenko.games.bricks.UserProfile;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class UpdateStarsTest {

    private final UserProfile userProfile = mock();
    private final UpdateStars updateStars = new UpdateStars(userProfile);

    @Test
    void givenLevelCompleted_thenStarsUpdated() {
        updateStars.onEvent(new LevelCompleted("easy", 1, 2));
        verify(userProfile).updateStars("easy", 1, 2);
    }

    @Test
    void givenOtherEvent_thenStarsNotUpdated() {
        updateStars.onEvent(new AssetsLoaded());
        verifyNoInteractions(userProfile);
    }
}
