package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.maximtereshchenko.games.common.configuration.ConfigurationDeserializers;
import com.github.maximtereshchenko.games.common.configuration.ConfigurationReader;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.snakes.configuration.Configuration;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvent;
import com.github.maximtereshchenko.games.snakes.screen.ScreenFactory;
import com.github.maximtereshchenko.games.snakes.session.SessionFactory;
import tools.jackson.core.type.TypeReference;

import java.util.Set;

final class SnakesGameAdapter implements ApplicationListener {

    private SnakesGame original;

    static void main() {
        new Lwjgl3Application(new SnakesGameAdapter());
    }

    @Override
    public void create() {
        var configurationReader = new ConfigurationReader(
            new ConfigurationDeserializers()
        );
        var configuration = configurationReader.value(
            "configuration.json",
            new TypeReference<Configuration>() {}
        );
        var userProfile = new UserProfile(
            configuration,
            Gdx.app.getPreferences(configuration.preferencesName())
        );
        var assets = configuration.assets();
        var modes = configuration.modes();
        var eventBus = new EventBus<ApplicationEvent>();
        var shapeRenderer = new ShapeRenderer();
        var spriteBatch = new SpriteBatch();
        var assetManager = new AssetManager(new ClasspathFileHandleResolver());
        assets.loadingAssets().forEach(assetManager::load);
        assetManager.finishLoading();
        eventBus.subscribe(new StartMusic(userProfile, assetManager, assets));
        eventBus.subscribe(new UpdateUserProfileMetrics(userProfile, modes));
        eventBus.subscribe(new UnlockModes(userProfile, modes));
        var screenFactory = new ScreenFactory(
            assetManager,
            assets,
            spriteBatch,
            eventBus,
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
        eventBus.subscribe(original);
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
