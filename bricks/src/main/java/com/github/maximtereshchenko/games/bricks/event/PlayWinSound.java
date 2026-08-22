package com.github.maximtereshchenko.games.bricks.event;

import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.event.Subscriber;

public final class PlayWinSound implements Subscriber<Event> {

    private final Configuration configuration;
    private final AssetManager assetManager;

    public PlayWinSound(
        Configuration configuration,
        AssetManager assetManager
    ) {
        this.configuration = configuration;
        this.assetManager = assetManager;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof LevelCompleted) {
            assetManager.get(
                    configuration.assets()
                        .winSound()
                )
                .play();
        }
    }
}
