package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Set;

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
        Assets.ALL.forEach(assetManager::load);
        assetManager.finishLoading(); //TODO lazy screen
        var stageFactory = new StageFactory(
            assetManager,
            spriteBatch,
            applicationEvents
        );
        var progressBar = new ProgressBar(0, 1, 0.01f, false, assetManager.<Skin>finishLoadingAsset(Assets.SKIN));
        var loadingStage = stageFactory.loadingStage(progressBar);
        var titleStage = stageFactory.titleStage();
        var snakesGame = new SnakesGame(
            new LoadingScreen(
                loadingStage,
                assetManager,
                progressBar,
                applicationEvents
            ),
            new TitleScreen(titleStage),
            new SnakeSessionScreen(
                new SnakeSessionFactory(),
                worldDimensions,
                shapeRenderer,
                new FitViewport(worldDimensions.width(), worldDimensions.height()),
                applicationEvents
            ),
            Set.of(
                shapeRenderer,
                spriteBatch,
                assetManager,
                titleStage,
                loadingStage
            )
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
}
