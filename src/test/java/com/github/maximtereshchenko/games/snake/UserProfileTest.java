package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Preferences;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class UserProfileTest {

    private final Preferences preferences = mock();
    private final UserProfile userProfile = new UserProfile(preferences);

    @Test
    void givenClassicMode_whenIsUnlocked_thenTrue() {
        assertThat(userProfile.isUnlocked(Mode.CLASSIC)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = Mode.class, names = "CLASSIC", mode = EnumSource.Mode.EXCLUDE)
    void givenMode_whenIsUnlocked_thenDelegateToPreferences(Mode mode) {
        when(preferences.getBoolean(mode.toString())).thenReturn(true);
        assertThat(userProfile.isUnlocked(mode)).isTrue();
    }

    @Test
    void whenUnlock_thenStorePreference() {
        userProfile.unlock(Mode.CLASSIC);
        verify(preferences).putBoolean(Mode.CLASSIC.toString(), true);
    }

    @Test
    void whenSave_thenFlushPreferences() {
        userProfile.save();
        verify(preferences).flush();
    }
}