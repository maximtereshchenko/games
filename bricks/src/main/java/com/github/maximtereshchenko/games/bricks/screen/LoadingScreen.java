package com.github.maximtereshchenko.games.bricks.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.event.AssetsLoaded;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.screen.view.LoadingView;
import com.github.maximtereshchenko.games.event.EventBus;

final class LoadingScreen implements Screen {

    private final Screen original;
    private final Configuration configuration;
    private final LoadingView loadingView;
    private final AssetManager assetManager;
    private final EventBus<Event> eventBus;

    LoadingScreen(
        Screen original,
        Configuration configuration,
        LoadingView loadingView,
        AssetManager assetManager,
        EventBus<Event> eventBus
    ) {
        this.original = original;
        this.configuration = configuration;
        this.loadingView = loadingView;
        this.assetManager = assetManager;
        this.eventBus = eventBus;
    }

    @Override
    public void show() {
        configuration.assets()
            .gameAssets()
            .forEach(assetManager::load);
        original.show();
    }

    @Override
    public void render(float delta) {
        var loaded = assetManager.update();
        loadingView.updateProgress(assetManager.getProgress());
        original.render(delta);
        if (loaded) {
            eventBus.publish(new AssetsLoaded());
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
