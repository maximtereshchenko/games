package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.github.maximtereshchenko.games.snakes.event.AssetsLoaded;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class StartMusicTest {

    private final AssetManager assetManager = mock();
    private final Assets assets = mock();
    private final AssetDescriptor<Music> assetDescriptor = mock();
    private final Music music = mock();
    private final StartMusic startMusic = new StartMusic(
        assetManager,
        assets
    );

    @Test
    void givenAssetsLoaded_thenStartMusic() {
        when(assets.music()).thenReturn(assetDescriptor);
        when(assetManager.get(assetDescriptor)).thenReturn(music);
        startMusic.onEvent(new AssetsLoaded());
        verify(music).setLooping(true);
        verify(music).play();
    }
}