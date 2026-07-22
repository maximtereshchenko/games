package com.github.maximtereshchenko.games.snakes;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.maximtereshchenko.games.snakes.configuration.Configuration;
import com.github.maximtereshchenko.games.snakes.configuration.ConfigurationModule;
import com.github.maximtereshchenko.games.snakes.event.ApplicationEvents;
import com.github.maximtereshchenko.games.snakes.screen.ScreenFactory;
import com.github.maximtereshchenko.games.snakes.session.EntityFactory;
import com.github.maximtereshchenko.games.snakes.session.SnakeSessionFactory;
import dev.dominion.ecs.engine.system.Config;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Set;

final class SnakesGameAdapter implements ApplicationListener {

    private SnakesGame original;

    static void main() {
        java.lang.System.setProperty(
            Config.getPropertyName(Config.SHOW_BANNER),
            Boolean.toString(false)
        );
        new Lwjgl3Application(new SnakesGameAdapter());
    }

    @Override
    public void create() {
        var configuration = configuration();
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
        applicationEvents.subscribe(new IncrementStatistics(userProfile));
        applicationEvents.subscribe(new UnlockModes(userProfile, modes));
        var screenFactory = new ScreenFactory(
            configuration,
            assetManager,
            assets,
            spriteBatch,
            applicationEvents,
            userProfile,
            new SnakeSessionFactory(
                configuration,
                shapeRenderer,
                spriteBatch,
                assetManager,
                assets
            ),
            new EntityFactory(configuration),
            modes
        );
        original = new SnakesGame(
            screenFactory,
            configuration.worldDimensions(),
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

    private Configuration configuration() {
        try (var reader = Gdx.files.classpath("configuration.json").reader()) {
            return JsonMapper.builder()
                .addModule(new ConfigurationModule())
                .build()
                .readValue(reader, Configuration.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
