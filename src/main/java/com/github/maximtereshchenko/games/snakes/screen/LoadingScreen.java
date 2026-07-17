package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.games.snakes.Assets;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.games.snakes.event.AssetsLoaded;
import com.github.maximtereshchenko.games.snakes.screen.view.LoadingView;

final class LoadingScreen implements Screen {

    private final Screen original;
    private final LoadingView loadingView;
    private final AssetManager assetManager;
    private final ApplicationEvents applicationEvents;
    private final Assets assets;

    LoadingScreen(
        Screen original,
        LoadingView loadingView,
        AssetManager assetManager,
        ApplicationEvents applicationEvents,
        Assets assets
    ) {
        this.original = original;
        this.loadingView = loadingView;
        this.assetManager = assetManager;
        this.applicationEvents = applicationEvents;
        this.assets = assets;
    }

    @Override
    public void show() {
        assets.gameAssets().forEach(assetManager::load);
        original.show();
    }

    @Override
    public void render(float delta) {
        var loaded = assetManager.update();
        loadingView.updateProgress(assetManager.getProgress());
        original.render(delta);
        if (loaded) {
            applicationEvents.publish(new AssetsLoaded());
        }
    }

    @Override
    public void resize(int width, int height) {
        original.resize(width, height);
    }

    @Override
    public void pause() {
        original.pause();
    }

    @Override
    public void resume() {
        original.resume();
    }

    @Override
    public void hide() {
        original.hide();
    }

    @Override
    public void dispose() {
        original.dispose();
    }
}
