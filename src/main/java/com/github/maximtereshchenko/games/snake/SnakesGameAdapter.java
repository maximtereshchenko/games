package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.dominion.ecs.engine.system.Config;

import java.util.Set;

final class SnakesGameAdapter implements ApplicationListener {

    private ApplicationListener original;

    static void main() {
        System.setProperty(
            Config.getPropertyName(Config.SHOW_BANNER),
            Boolean.toString(false)
        );
        new Lwjgl3Application(new SnakesGameAdapter());
    }

    @Override
    public void create() {
        var applicationEvents = new ApplicationEvents();
        var shapeRenderer = new ShapeRenderer();
        var spriteBatch = new SpriteBatch();
        var assetManager = new AssetManager(new ClasspathFileHandleResolver());
        Assets.LOADING_ASSETS.forEach(assetManager::load);
        assetManager.finishLoading();
        original = new SnakesGame(
            new ScreenFactory(
                assetManager,
                spriteBatch,
                shapeRenderer,
                applicationEvents
            ),
            new WorldDimensions(33, 16),
            Set.of(
                shapeRenderer,
                spriteBatch,
                assetManager
            ),
            applicationEvents
        );
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
