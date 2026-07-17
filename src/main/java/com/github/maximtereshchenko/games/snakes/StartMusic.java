package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.games.snakes.event.AssetsLoaded;
import com.github.maximtereshchenko.games.snakes.event.Subscriber;

final class StartMusic implements Subscriber {

    private final AssetManager assetManager;
    private final Assets assets;

    StartMusic(AssetManager assetManager, Assets assets) {
        this.assetManager = assetManager;
        this.assets = assets;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof AssetsLoaded) {
            var music = assetManager.get(assets.music());
            music.setLooping(true);
            music.setVolume(0.1f);
            music.play();
        }
    }
}
