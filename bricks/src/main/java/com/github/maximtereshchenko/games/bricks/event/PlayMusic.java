package com.github.maximtereshchenko.games.bricks.event;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.event.Subscriber;

import java.util.Optional;

public final class PlayMusic implements Subscriber<Event> {

    private final Configuration configuration;
    private final AssetManager assetManager;
    private Music music;

    public PlayMusic(
        Configuration configuration,
        AssetManager assetManager
    ) {
        this.configuration = configuration;
        this.assetManager = assetManager;
    }

    @Override
    public void onEvent(Event event) {
        nextMusic(event)
            .ifPresent(this::play);
    }

    private void play(AssetDescriptor<Music> assetDescriptor) {
        if (music != null) {
            music.stop();
        }
        music = assetManager.get(assetDescriptor);
        music.setLooping(true);
        music.play();
    }

    private Optional<AssetDescriptor<Music>> nextMusic(Event event) {
        var assets = configuration.assets();
        return switch (event) {
            case AssetsLoaded _,
                 LevelCompleted _,
                 LevelFailed _ -> Optional.of(assets.mainMusic());
            case LevelSelected _ -> Optional.of(assets.sessionMusic());
            default -> Optional.empty();
        };
    }
}
