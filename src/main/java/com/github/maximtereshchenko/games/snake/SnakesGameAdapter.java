package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

final class SnakesGameAdapter implements ApplicationListener {

    private ApplicationListener original;

    static void main() {
        new Lwjgl3Application(new SnakesGameAdapter());
    }

    @Override
    public void create() {
        var worldDimensions = new WorldDimensions(6, 6);
        var applicationEvents = new ApplicationEvents();
        var shapeRenderer = new ShapeRenderer();
        var spriteBatch = new SpriteBatch();
        var assetManager = new AssetManager();
        Assets.LOADING_ASSETS.forEach(assetManager::load);
        assetManager.finishLoading();
        var stageFactory = new StageFactory(
            assetManager,
            spriteBatch,
            applicationEvents
        );
        var progressBar = new ProgressBar(0, 1, 0.01f, false, assetManager.<Skin>finishLoadingAsset(Assets.SKIN));
        var loadingStage = stageFactory.loadingStage(progressBar);
        var disposables = new Disposables();
        disposables.add(
            shapeRenderer,
            spriteBatch,
            assetManager,
            loadingStage
        );
        var snakesGame = new SnakesGame(
            new LoadingScreen(
                new StageScreen(loadingStage),
                assetManager,
                progressBar,
                applicationEvents,
                Assets.GAME_ASSETS
            ),
            new LazyScreen(() -> titleScreen(stageFactory, disposables)),
            new SnakeSessionScreen(
                new SnakeSessionFactory(),
                worldDimensions,
                shapeRenderer,
                new FitViewport(worldDimensions.width(), worldDimensions.height()),
                applicationEvents
            ),
            disposables
        );
        applicationEvents.subscribe(snakesGame);
        original = snakesGame;
    }

    @Override
    public void resize(int width, int height) {
        original.resize(width, height);
    }

    @Override
    public void render() {
        original.render();
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
    public void dispose() {
        original.dispose();
    }

    private Screen titleScreen(StageFactory stageFactory, Disposables disposables) {
        var titleStage = stageFactory.titleStage();
        disposables.add(titleStage);
        return new StageScreen(titleStage);
    }
}
