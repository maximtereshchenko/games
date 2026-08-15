package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.games.event.Subscriber;
import com.github.maximtereshchenko.games.snakes.configuration.Assets;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.games.snakes.event.AssetsLoaded;

final class StartMusic implements Subscriber<ApplicationEvent> {

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
        if (!(applicationEvent instanceof AssetsLoaded)) {
            return;
        }
        var music = assetManager.get(assets.music());
        music.setLooping(true);
        music.setVolume(userProfile.musicVolume());
        music.play();
    }
}
