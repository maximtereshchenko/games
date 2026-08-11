package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.maximtereshchenko.games.snakes.configuration.ConfigurationReader;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.games.snakes.screen.ScreenFactory;
import com.github.maximtereshchenko.games.snakes.session.SessionFactory;

import java.util.Set;

final class SnakesGameAdapter implements ApplicationListener {

    private SnakesGame original;

    static void main() {
        new Lwjgl3Application(new SnakesGameAdapter());
    }

    @Override
    public void create() {
        var configurationReader = new ConfigurationReader();
        var configuration = configurationReader.configuration(
            "configuration.json"
        );
        var userProfile = new UserProfile(
            configuration,
            Gdx.app.getPreferences(configuration.preferencesName())
        );
        var assets = configuration.assets();
        var modes = configuration.modes();
        var applicationEvents = new ApplicationEvents();
        var shapeRenderer = new ShapeRenderer();
        var spriteBatch = new SpriteBatch();
        var assetManager = new AssetManager(new ClasspathFileHandleResolver());
        assets.loadingAssets().forEach(assetManager::load);
        assetManager.finishLoading();
        applicationEvents.subscribe(new StartMusic(userProfile, assetManager, assets));
        applicationEvents.subscribe(new UpdateUserProfileMetrics(userProfile, modes));
        applicationEvents.subscribe(new UnlockModes(userProfile, modes));
        var screenFactory = new ScreenFactory(
            assetManager,
            assets,
            spriteBatch,
            applicationEvents,
            userProfile,
            new SessionFactory(
                configurationReader,
                shapeRenderer,
                spriteBatch,
                assetManager,
                assets
            ),
            modes
        );
        original = new SnakesGame(
            screenFactory,
            Set.of(
                shapeRenderer,
                spriteBatch,
                assetManager
            ),
            userProfile
        );
        applicationEvents.subscribe(original);
        original.setScreen(screenFactory.loadingScreen());
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
