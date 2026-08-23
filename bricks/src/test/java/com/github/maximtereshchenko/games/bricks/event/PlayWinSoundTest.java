package com.github.maximtereshchenko.games.bricks.event;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class PlayWinSoundTest {

    private final Configuration configuration = mock();
    private final Configuration.Assets assets = mock();
    private final UserProfile userProfile = mock();
    private final AssetManager assetManager = mock();
    private final AssetDescriptor<Sound> winSound = mock();
    private final Sound sound = mock();
    private final PlayWinSound playWinSound = new PlayWinSound(
        configuration,
        userProfile,
        assetManager
    );

    @Test
    void givenLevelCompleted_thenWinSoundPlayed() {
        when(configuration.assets()).thenReturn(assets);
        when(assets.winSound()).thenReturn(winSound);
        when(assetManager.get(winSound)).thenReturn(sound);
        when(userProfile.soundVolume()).thenReturn(0.4f);
        playWinSound.onEvent(new LevelCompleted("easy", 1, 3));
        verify(sound).play(0.4f);
    }

    @Test
    void givenOtherEvent_thenWinSoundNotPlayed() {
        playWinSound.onEvent(new LevelFailed());
        verifyNoInteractions(assetManager);
    }
}
