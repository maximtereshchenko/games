package com.github.maximtereshchenko.games.bricks.event;

import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

final class UnlockFirstLevelsTest {

    private final Configuration configuration = mock();
    private final UserProfile userProfile = mock();
    private final UnlockFirstLevels unlockFirstLevels =
        new UnlockFirstLevels(configuration, userProfile);

    @Test
    void givenAssetsLoaded_thenFirstLevelsUnlocked() {
        when(configuration.difficulties()).thenReturn(
            Map.of("easy", "easy.json", "hard", "hard.json")
        );
        unlockFirstLevels.onEvent(new AssetsLoaded());
        verify(userProfile).unlock("easy", 0);
        verify(userProfile).unlock("hard", 0);
    }

    @Test
    void givenOtherEvent_thenLevelsNotUnlocked() {
        unlockFirstLevels.onEvent(new SettingsRequested());
        verifyNoInteractions(userProfile);
    }
}
