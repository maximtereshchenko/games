package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

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
        var assetManager = new AssetManager(new ClasspathFileHandleResolver());
        Assets.LOADING_ASSETS.forEach(assetManager::load);
        assetManager.finishLoading();
        var stageFactory = new StageFactory(
            assetManager,
            spriteBatch,
            applicationEvents
        );
        var loadingStage = stageFactory.loadingStage();
        var disposables = new Disposables();
        disposables.add(
            shapeRenderer,
            spriteBatch,
            assetManager,
            loadingStage
        );
        var snakeSessionScreen = new SnakeSessionScreen(
            worldDimensions,
            shapeRenderer,
            new FitViewport(worldDimensions.width(), worldDimensions.height()),
            applicationEvents
        );
        var snakesGame = new SnakesGame(
            new LoadingScreen(
                new StageScreen(loadingStage),
                assetManager,
                loadingStage.getRoot().findActor(StageFactory.ASSETS_LOADING_BAR),
                applicationEvents,
                Assets.GAME_ASSETS
            ),
            lazyScreen(stageFactory::titleStage, disposables),
            lazyScreen(() -> stageFactory.modeSelectionStage(modes()), disposables),
            snakeSessionScreen,
            disposables
        );
        applicationEvents.subscribe(snakeSessionScreen);
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

    private Screen lazyScreen(Supplier<Stage> supplier, Disposables disposables) {
        return new LazyScreen(() -> {
            var stage = supplier.get();
            disposables.add(stage);
            return new StageScreen(stage);
        });
    }

    private LinkedHashMap<Mode, SnakeSessionFactory> modes() {
        var modes = new LinkedHashMap<Mode, SnakeSessionFactory>();
        modes.put(Mode.CLASSIC, new SnakeSessionFactory());
        return modes;
    }
}
