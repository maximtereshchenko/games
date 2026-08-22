package com.github.maximtereshchenko.games.bricks;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.bricks.configuration.*;
import com.github.maximtereshchenko.games.bricks.event.*;
import com.github.maximtereshchenko.games.bricks.screen.ScreenFactory;
import com.github.maximtereshchenko.games.bricks.session.BricksBlueprints;
import com.github.maximtereshchenko.games.bricks.session.PhysicsObjectFactory;
import com.github.maximtereshchenko.games.bricks.session.SessionFactory;
import com.github.maximtereshchenko.games.event.EventBus;
import tools.jackson.core.type.TypeReference;

import java.util.Set;

final class BricksGameAdapter implements ApplicationListener {

    private BricksGame original;

    static void main() {
        World.setVelocityThreshold(0);
        new Lwjgl3Application(new BricksGameAdapter());
    }

    @Override
    public void create() {
        var spriteBatch = new SpriteBatch();
        var eventBus = new EventBus<Event>();
        var configurationReader = new ConfigurationReader(
            configurationDeserializers()
        );
        var configuration = configurationReader.value(
            "configuration.json",
            new TypeReference<Configuration>() {}
        );
        var assetManager = assetManager(configuration);
        var userProfile = new UserProfile(
            configuration,
            Gdx.app.getPreferences(
                configuration.preferencesName()
            )
        );
        var screenFactory = screenFactory(
            configuration,
            userProfile,
            assetManager,
            eventBus,
            configurationReader,
            spriteBatch
        );
        setWindowedMode(configuration);
        eventBus.subscribe(
            new UnlockFirstLevels(
                configuration,
                userProfile
            )
        );
        eventBus.subscribe(
            new UnlockNextLevel(userProfile)
        );
        eventBus.subscribe(
            new UpdateStars(userProfile)
        );
        eventBus.subscribe(
            new PlayMusic(
                configuration,
                userProfile,
                assetManager
            )
        );
        eventBus.subscribe(
            new PlayWinSound(
                configuration,
                userProfile,
                assetManager
            )
        );
        original = new BricksGame(
            screenFactory,
            userProfile,
            Set.of(
                spriteBatch,
                assetManager
            )
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

    private void setWindowedMode(Configuration configuration) {
        var dimensions = configuration.interfaceDimensions();
        Gdx.graphics.setWindowedMode(
            Math.round(dimensions.width()),
            Math.round(dimensions.height())
        );
    }

    private ScreenFactory screenFactory(
        Configuration configuration,
        UserProfile userProfile,
        AssetManager assetManager,
        EventBus<Event> eventBus,
        ConfigurationReader configurationReader,
        SpriteBatch spriteBatch
    ) {
        return new ScreenFactory(
            configuration,
            userProfile,
            assetManager,
            eventBus,
            configurationReader,
            new BricksBlueprints(),
            new SessionFactory(
                configuration,
                eventBus,
                new PhysicsObjectFactory(configuration),
                assetManager,
                userProfile
            ),
            spriteBatch
        );
    }

    private AssetManager assetManager(Configuration configuration) {
        var assetManager = new AssetManager(
            new ClasspathFileHandleResolver()
        );
        configuration.assets()
            .loadingAssets()
            .forEach(assetManager::load);
        assetManager.finishLoading();
        return assetManager;
    }

    private ConfigurationDeserializers configurationDeserializers() {
        var configurationDeserializers =
            new ConfigurationDeserializers();
        configurationDeserializers.addDeserializer(
                CellDefinition.class,
                new CellDefinitionDeserializer()
            )
            .addDeserializer(
                AssetDescriptor.class,
                new AssetDescriptorDeserializer()
            );
        return configurationDeserializers;
    }
}
