package com.github.maximtereshchenko.games.bricks.event.subscriber;

import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.event.LevelCompleted;
import com.github.maximtereshchenko.games.common.event.Subscriber;

public final class PlayWinSound implements Subscriber<Event> {

    private final Configuration configuration;
    private final UserProfile userProfile;
    private final AssetManager assetManager;

    public PlayWinSound(
        Configuration configuration,
        UserProfile userProfile,
        AssetManager assetManager
    ) {
        this.configuration = configuration;
        this.userProfile = userProfile;
        this.assetManager = assetManager;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof LevelCompleted) {
            assetManager.get(
                    configuration.assets()
                        .winSound()
                )
                .play(userProfile.soundVolume());
        }
    }
}
