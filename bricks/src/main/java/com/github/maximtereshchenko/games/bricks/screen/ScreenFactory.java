package com.github.maximtereshchenko.games.bricks.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.maximtereshchenko.games.bricks.UserProfile;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.event.*;
import com.github.maximtereshchenko.games.bricks.screen.view.*;
import com.github.maximtereshchenko.games.bricks.screen.view.settings.SettingsView;
import com.github.maximtereshchenko.games.bricks.session.BricksBlueprints;
import com.github.maximtereshchenko.games.bricks.session.SessionFactory;
import com.github.maximtereshchenko.games.common.configuration.ConfigurationReader;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.screen.StageScreen;
import tools.jackson.core.type.TypeReference;

public final class ScreenFactory {

    private final Configuration configuration;
    private final UserProfile userProfile;
    private final AssetManager assetManager;
    private final EventBus<Event> eventBus;
    private final ConfigurationReader configurationReader;
    private final BricksBlueprints bricksBlueprints;
    private final SessionFactory sessionFactory;
    private final SpriteBatch spriteBatch;

    public ScreenFactory(
        Configuration configuration,
        UserProfile userProfile,
        AssetManager assetManager,
        EventBus<Event> eventBus,
        ConfigurationReader configurationReader,
        BricksBlueprints bricksBlueprints,
        SessionFactory sessionFactory,
        SpriteBatch spriteBatch
    ) {
        this.configuration = configuration;
        this.userProfile = userProfile;
        this.assetManager = assetManager;
        this.eventBus = eventBus;
        this.configurationReader = configurationReader;
        this.bricksBlueprints = bricksBlueprints;
        this.sessionFactory = sessionFactory;
        this.spriteBatch = spriteBatch;
    }

    public Screen loadingScreen() {
        var loadingView = new LoadingView(
            assetManager.get(configuration.assets().loadingBundle()),
            assetManager.get(configuration.assets().skin()),
            configuration,
            assetManager
        );
        return new LoadingScreen(
            stageScreen(loadingView),
            configuration,
            loadingView,
            assetManager,
            eventBus
        );
    }

    public Screen mainScreen() {
        var bundle = assetManager.get(configuration.assets().gameBundle());
        var skin = assetManager.get(configuration.assets().skin());
        var mainView = new MainView(
            bundle,
            skin,
            configuration,
            assetManager
        );
        mainView.onPlay(
            () -> eventBus.publish(new DifficultySelectionRequested())
        );
        mainView.onSettings(
            () -> eventBus.publish(new SettingsRequested())
        );
        return stageScreen(mainView);
    }

    public Screen settingsScreen() {
        var assets = configuration.assets();
        var settingsView = new SettingsView(
            assetManager.get(assets.gameBundle()),
            assetManager.get(assets.skin()),
            configuration,
            userProfile,
            assetManager
        );
        settingsView.onMusicVolumeChange(this::setMusicVolume);
        settingsView.onSoundVolumeChange(userProfile::updateSoundVolume);
        settingsView.onFinish(
            () -> eventBus.publish(new SettingsScreenFinished())
        );
        return stageScreen(settingsView);
    }

    public Screen difficultySelectionScreen() {
        var difficultySelectionView = new DifficultySelectionView(
            assetManager.get(configuration.assets().gameBundle()),
            assetManager.get(configuration.assets().skin()),
            configuration,
            assetManager
        );
        difficultySelectionView.onDifficultySelected(
            difficulty -> eventBus.publish(
                new DifficultySelected(difficulty)
            )
        );
        return stageScreen(difficultySelectionView);
    }

    public Screen levelSelectionScreen(String difficulty) {
        var levelSelectionView = new LevelSelectionView(
            assetManager.get(configuration.assets().skin()),
            configuration,
            userProfile,
            assetManager,
            difficulty
        );
        levelSelectionView.onLevelSelected(
            level -> eventBus.publish(
                new LevelSelected(difficulty, level)
            )
        );
        return stageScreen(levelSelectionView);
    }

    public Screen sessionScreen(
        String difficulty,
        int level
    ) {
        var dimensions = configuration.worldDimensions();
        var viewport = new FitViewport(
            dimensions.width(),
            dimensions.height()
        );
        var physicsWorld = new World(Vector2.Zero, true);
        var textureAtlas = assetManager.get(
            configuration.assets().textureAtlas()
        );
        var livesIndicator = new Indicator(
            textureAtlas.findRegion(
                configuration.livesIndicatorTexture()
            )
        );
        var starsIndicator = new Indicator(
            textureAtlas.findRegion(
                configuration.starsIndicatorTexture()
            )
        );
        return new SessionScreen(
            stageScreen(
                new SessionView(
                    configuration,
                    starsIndicator,
                    livesIndicator,
                    viewport,
                    sessionFactory.registry(
                        viewport,
                        livesIndicator,
                        starsIndicator,
                        bricksBlueprints.blueprints(
                                configurationReader.value(
                                    configuration.commonBlueprints(),
                                    new TypeReference<>() {}
                                )
                            )
                            .merged(
                                configurationReader.value(
                                    configuration.difficulties()
                                        .get(difficulty),
                                    new TypeReference<>() {}
                                )
                            ),
                        configurationReader.value(
                            configuration.levels()
                                .get(level),
                            new TypeReference<>() {}
                        ),
                        physicsWorld,
                        difficulty,
                        level
                    ),
                    assetManager
                )
            ),
            physicsWorld
        );
    }

    private void setMusicVolume(float volume) {
        var assets = configuration.assets();
        assetManager.get(assets.mainMusic()).setVolume(volume);
        assetManager.get(assets.sessionMusic()).setVolume(volume);
        userProfile.updateMusicVolume(volume);
    }

    private Screen stageScreen(Root root) {
        var dimensions = configuration.interfaceDimensions();
        var stage = new Stage(
            new FitViewport(
                dimensions.width(),
                dimensions.height()
            ),
            spriteBatch
        );
        stage.addActor(root);
        return new StageScreen(stage);
    }
}
