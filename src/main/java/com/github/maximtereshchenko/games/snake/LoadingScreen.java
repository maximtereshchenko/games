package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

import java.util.Set;

final class LoadingScreen implements Screen {

    private final StageScreen stageScreen;
    private final AssetManager assetManager;
    private final ProgressBar progressBar;
    private final ApplicationEvents applicationEvents;
    private final Set<AssetDescriptor<?>> assetDescriptors;

    LoadingScreen(
        StageScreen stageScreen,
        AssetManager assetManager,
        ProgressBar progressBar,
        ApplicationEvents applicationEvents,
        Set<AssetDescriptor<?>> assetDescriptors
    ) {
        this.stageScreen = stageScreen;
        this.assetManager = assetManager;
        this.progressBar = progressBar;
        this.applicationEvents = applicationEvents;
        this.assetDescriptors = assetDescriptors;
    }

    @Override
    public void show() {
        assetDescriptors.forEach(assetManager::load);
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
