package com.github.maximtereshchenko.snakes;

import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.snakes.configuration.Assets;
import com.github.maximtereshchenko.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.snakes.event.AssetsLoaded;
import com.github.maximtereshchenko.snakes.event.Subscriber;

final class StartMusic implements Subscriber {

    private final UserProfile userProfile;
    private final AssetManager assetManager;
    private final Assets assets;

    StartMusic(
        UserProfile userProfile,
        AssetManager assetManager,
        Assets assets
    ) {
        this.userProfile = userProfile;
        this.assetManager = assetManager;
        this.assets = assets;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof AssetsLoaded) {
            var music = assetManager.get(assets.music());
            music.setLooping(true);
            music.setVolume(userProfile.musicVolume());
            music.play();
        }
    }
}
