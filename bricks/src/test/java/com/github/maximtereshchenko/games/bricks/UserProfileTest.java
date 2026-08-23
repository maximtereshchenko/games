package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.Preferences;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class UserProfileTest {

    private final Configuration configuration = mock();
    private final Preferences preferences = mock();
    private final UserProfile userProfile = new UserProfile(configuration, preferences);

    @Test
    void whenIsUnlocked_thenBooleanFromPreferences() {
        when(preferences.getBoolean("levels.easy.1.unlocked")).thenReturn(true);
        assertThat(userProfile.isUnlocked("easy", 1)).isTrue();
    }

    @Test
    void whenUnlock_thenPutTrueToPreferences() {
        userProfile.unlock("easy", 1);
        verify(preferences).putBoolean("levels.easy.1.unlocked", true);
    }

    @Test
    void whenStars_thenIntFromPreferences() {
        when(preferences.getInteger("levels.easy.1.stars")).thenReturn(2);
        assertThat(userProfile.stars("easy", 1)).isEqualTo(2);
    }

    @Test
    void whenUpdateStars_thenPutIntToPreferences() {
        userProfile.updateStars("easy", 1, 3);
        verify(preferences).putInteger("levels.easy.1.stars", 3);
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
    void whenSoundVolume_thenFloatFromPreferencesWithDefaultFromConfiguration() {
        when(configuration.defaultSoundVolume()).thenReturn(0.8f);
        when(preferences.getFloat("sound.volume", 0.8f)).thenReturn(0.2f);
        assertThat(userProfile.soundVolume()).isEqualTo(0.2f);
    }

    @Test
    void whenUpdateSoundVolume_thenPutFloatToPreferences() {
        userProfile.updateSoundVolume(0.4f);
        verify(preferences).putFloat("sound.volume", 0.4f);
    }

    @Test
    void whenSave_thenFlushPreferences() {
        userProfile.save();
        verify(preferences).flush();
    }
}
