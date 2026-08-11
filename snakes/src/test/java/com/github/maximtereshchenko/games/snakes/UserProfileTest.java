package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.Preferences;
import com.github.maximtereshchenko.games.snakes.configuration.Configuration;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class UserProfileTest {

    private final Configuration configuration = mock();
    private final Preferences preferences = mock();
    private final Mode mode = mock();
    private final UserProfile userProfile = new UserProfile(configuration, preferences);

    @Test
    void whenIsUnlocked_thenBooleanFromPreferences() {
        when(mode.name()).thenReturn("mode");
        when(preferences.getBoolean("mode")).thenReturn(true);
        assertThat(userProfile.isUnlocked(mode)).isTrue();
    }

    @Test
    void whenUnlock_thenPutTrueToPreferences() {
        when(mode.name()).thenReturn("mode");
        userProfile.unlock(mode);
        verify(preferences).putBoolean("mode", true);
    }

    @Test
    void whenValue_thenIntFromPreferences() {
        userProfile.value(UserProfileMetric.LAUNCHES);
        verify(preferences).getInteger(UserProfileMetric.LAUNCHES.name());
    }

    @Test
    void whenMusicVolume_thenFloatFromPreferencesWithDefaultFromConfiguration() {
        when(configuration.defaultMusicVolume()).thenReturn(0.5f);
        when(preferences.getFloat("music.volume", 0.5f)).thenReturn(0.3f);
        assertThat(userProfile.musicVolume()).isEqualTo(0.3f);
    }

    @Test
    void whenUpdateMusicVolume_thenPutFloatToPreferences() {
        userProfile.updateMusicVolume(0.5f);
        verify(preferences).putFloat("music.volume", 0.5f);
    }

    @Test
    void whenSave_thenFlushPreferences() {
        userProfile.save();
        verify(preferences).flush();
    }

    @Test
    void whenUpdate_thenPutIntToPreferences() {
        userProfile.update(UserProfileMetric.MODES_UNLOCKED, 1);
        verify(preferences).putInteger(UserProfileMetric.MODES_UNLOCKED.name(), 1);
    }

    @Test
    void whenIncrement_thenPutIntToPreferences() {
        when(preferences.getInteger(UserProfileMetric.LAUNCHES.name())).thenReturn(1);
        userProfile.increment(UserProfileMetric.LAUNCHES);
        verify(preferences).putInteger(UserProfileMetric.LAUNCHES.name(), 2);
    }
}