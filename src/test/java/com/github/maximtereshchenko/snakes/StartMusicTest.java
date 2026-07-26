package com.github.maximtereshchenko.snakes;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.github.maximtereshchenko.snakes.configuration.Assets;
import com.github.maximtereshchenko.snakes.event.AssetsLoaded;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class StartMusicTest {

    private final UserProfile userProfile = mock();
    private final AssetManager assetManager = mock();
    private final Assets assets = mock();
    private final AssetDescriptor<Music> assetDescriptor = mock();
    private final Music music = mock();
    private final StartMusic startMusic = new StartMusic(
        userProfile,
        assetManager,
        assets
    );

    @Test
    void givenAssetsLoaded_thenStartMusic() {
        when(assets.music()).thenReturn(assetDescriptor);
        when(assetManager.get(assetDescriptor)).thenReturn(music);
        when(userProfile.musicVolume()).thenReturn(0.5f);
        startMusic.onEvent(new AssetsLoaded());
        verify(music).setLooping(true);
        verify(music).setVolume(0.5f);
        verify(music).play();
    }
}