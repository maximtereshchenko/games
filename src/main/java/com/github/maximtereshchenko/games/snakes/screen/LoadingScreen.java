package com.github.maximtereshchenko.games.snakes.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.maximtereshchenko.games.snakes.Assets;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.games.snakes.event.AssetsLoaded;

final class LoadingScreen implements Screen {

    private final StageScreen stageScreen;
    private final AssetManager assetManager;
    private final ProgressBar progressBar;
    private final ApplicationEvents applicationEvents;
    private final Assets assets;

    LoadingScreen(
        StageScreen stageScreen,
        AssetManager assetManager,
        ProgressBar progressBar,
        ApplicationEvents applicationEvents,
        Assets assets
    ) {
        this.stageScreen = stageScreen;
        this.assetManager = assetManager;
        this.progressBar = progressBar;
        this.applicationEvents = applicationEvents;
        this.assets = assets;
    }

    @Override
    public void show() {
        assets.gameAssets().forEach(assetManager::load);
        stageScreen.show();
    }

    @Override
    public void render(float delta) {
        var loaded = assetManager.update();
        progressBar.setValue(assetManager.getProgress());
        stageScreen.render(delta);
        if (loaded) {
            applicationEvents.publish(new AssetsLoaded());
        }
    }

    @Override
    public void resize(int width, int height) {
        stageScreen.resize(width, height);
    }

    @Override
    public void pause() {
        stageScreen.pause();
    }

    @Override
    public void resume() {
        stageScreen.resume();
    }

    @Override
    public void hide() {
        stageScreen.hide();
    }

    @Override
    public void dispose() {
        stageScreen.dispose();
    }
}
