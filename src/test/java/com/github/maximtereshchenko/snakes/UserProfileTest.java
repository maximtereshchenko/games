package com.github.maximtereshchenko.snakes;

import com.badlogic.gdx.Preferences;
import com.github.maximtereshchenko.snakes.configuration.Configuration;
import com.github.maximtereshchenko.snakes.configuration.Mode;
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
        userProfile.value(UserProfileStatistics.LAUNCHES);
        verify(preferences).getInteger(UserProfileStatistics.LAUNCHES.name());
    }

    @Test
    void whenUpdate_thenPutIntToPreferences() {
        userProfile.update(UserProfileStatistics.LAUNCHES, 1);
        verify(preferences).putInteger(UserProfileStatistics.LAUNCHES.name(), 1);
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
}