package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.Preferences;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class UserProfileTest {

    private final Preferences preferences = mock();
    private final UserProfile userProfile = new UserProfile(preferences);

    @Test
    void whenIsUnlocked_thenBooleanFromPreferences() {
        when(preferences.getBoolean("mode")).thenReturn(true);
        assertThat(userProfile.isUnlocked(new Mode("mode", 0, Set.of(), Map.of(), null)))
            .isTrue();
    }

    @Test
    void whenUnlock_thenPutTrueToPreferences() {
        userProfile.unlock(new Mode("mode", 0, Set.of(), Map.of(), null));
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
    void whenSave_thenFlushPreferences() {
        userProfile.save();
        verify(preferences).flush();
    }
}