package com.github.maximtereshchenko.games.bricks.event.subscriber;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.event.AssetsLoaded;
import com.github.maximtereshchenko.games.bricks.event.LevelSelected;
import com.github.maximtereshchenko.games.bricks.event.SettingsRequested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class PlayMusicTest {

    private final Configuration configuration = mock();
    private final Configuration.Assets assets = mock();
    private final UserProfile userProfile = mock();
    private final AssetManager assetManager = mock();
    private final AssetDescriptor<Music> mainMusic = mock();
    private final AssetDescriptor<Music> sessionMusic = mock();
    private final Music music = mock();
    private final PlayMusic playMusic = new PlayMusic(
        configuration,
        userProfile,
        assetManager
    );

    @BeforeEach
    void setUp() {
        when(configuration.assets()).thenReturn(assets);
        when(assets.mainMusic()).thenReturn(mainMusic);
        when(assets.sessionMusic()).thenReturn(sessionMusic);
        when(assetManager.get(mainMusic)).thenReturn(music);
        when(assetManager.get(sessionMusic)).thenReturn(music);
        when(userProfile.musicVolume()).thenReturn(0.5f);
    }

    @Test
    void givenAssetsLoaded_thenMainMusicPlayed() {
        playMusic.onEvent(new AssetsLoaded());
        verify(music).setLooping(true);
        verify(music).setVolume(0.5f);
        verify(music).play();
        verify(music, never()).stop();
    }

    @Test
    void givenLevelSelected_thenSessionMusicPlayed() {
        playMusic.onEvent(new LevelSelected("easy", 1));
        verify(assetManager).get(sessionMusic);
        verify(music).play();
    }

    @Test
    void givenMusicAlreadyPlaying_whenLevelSelected_thenPreviousMusicStopped() {
        playMusic.onEvent(new AssetsLoaded());
        playMusic.onEvent(new LevelSelected("easy", 1));
        verify(music).stop();
    }

    @Test
    void givenUnrelatedEvent_thenMusicNotPlayed() {
        playMusic.onEvent(new SettingsRequested());
        verifyNoInteractions(assetManager);
    }
}
