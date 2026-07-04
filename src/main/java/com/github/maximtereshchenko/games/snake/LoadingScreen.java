package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

final class LoadingScreen extends StageScreen {

    private final AssetManager assetManager;
    private final ProgressBar progressBar;
    private final ApplicationEvents applicationEvents;

    LoadingScreen(
        Stage stage,
        AssetManager assetManager,
        ProgressBar progressBar,
        ApplicationEvents applicationEvents
    ) {
        super(stage);
        this.assetManager = assetManager;
        this.progressBar = progressBar;
        this.applicationEvents = applicationEvents;
    }

    @Override
    void beforeRendering() {
        var loaded = assetManager.update();
        progressBar.setValue(assetManager.getProgress());
        if (loaded) {
            applicationEvents.publish(ApplicationEvent.ASSETS_LOADED);
        }
    }
}
