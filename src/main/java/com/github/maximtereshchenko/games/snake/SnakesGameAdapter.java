package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashSet;

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
        assetManager.finishLoading();//TODO loading screen
        var stageFactory = new StageFactory(
            assetManager,
            spriteBatch,
            applicationEvents
        );
        var titleStage = stageFactory.titleStage();
        var disposables = new HashSet<Disposable>();
        disposables.add(shapeRenderer);
        disposables.add(spriteBatch);
        disposables.add(assetManager);
        disposables.add(titleStage);
        var snakesGame = new SnakesGame(
            new StageScreen(titleStage),
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
}
