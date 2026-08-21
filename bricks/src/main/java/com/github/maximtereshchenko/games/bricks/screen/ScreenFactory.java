package com.github.maximtereshchenko.games.bricks.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import com.github.maximtereshchenko.games.bricks.configuration.ConfigurationReader;
import com.github.maximtereshchenko.games.bricks.event.DifficultySelected;
import com.github.maximtereshchenko.games.bricks.event.DifficultySelectionRequested;
import com.github.maximtereshchenko.games.bricks.event.Event;
import com.github.maximtereshchenko.games.bricks.event.LevelSelected;
import com.github.maximtereshchenko.games.bricks.screen.view.*;
import com.github.maximtereshchenko.games.bricks.session.BricksBlueprints;
import com.github.maximtereshchenko.games.bricks.session.SessionFactory;
import com.github.maximtereshchenko.games.event.EventBus;
import tools.jackson.core.type.TypeReference;

public final class ScreenFactory {

    private final Configuration configuration;
    private final AssetManager assetManager;
    private final EventBus<Event> eventBus;
    private final ConfigurationReader configurationReader;
    private final BricksBlueprints bricksBlueprints;
    private final SessionFactory sessionFactory;
    private final SpriteBatch spriteBatch;

    public ScreenFactory(
        Configuration configuration,
        AssetManager assetManager,
        EventBus<Event> eventBus,
        ConfigurationReader configurationReader,
        BricksBlueprints bricksBlueprints,
        SessionFactory sessionFactory,
        SpriteBatch spriteBatch
    ) {
        this.configuration = configuration;
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
            assetManager.get(configuration.assets().skin())
        );
        return new LoadingScreen(
            new StageScreen(
                spriteBatch,
                loadingView
            ),
            configuration,
            loadingView,
            assetManager,
            eventBus
        );
    }

    public Screen mainScreen() {
        var bundle = assetManager.get(configuration.assets().gameBundle());
        var skin = assetManager.get(configuration.assets().skin());
        var mainView = new MainView(bundle, skin);
        mainView.onPlay(
            () -> eventBus.publish(new DifficultySelectionRequested())
        );
        return new StageScreen(spriteBatch, mainView);
    }

    public Screen difficultySelectionScreen() {
        var difficultySelectionView = new DifficultySelectionView(
            assetManager.get(configuration.assets().gameBundle()),
            assetManager.get(configuration.assets().skin()),
            configuration
        );
        difficultySelectionView.onDifficultySelected(
            difficulty -> eventBus.publish(
                new DifficultySelected(difficulty)
            )
        );
        return new StageScreen(spriteBatch, difficultySelectionView);
    }

    public Screen levelSelectionScreen(String difficulty) {
        var levelSelectionView = new LevelSelectionView(
            assetManager.get(configuration.assets().skin()),
            configuration
        );
        levelSelectionView.onLevelSelected(
            level -> eventBus.publish(
                new LevelSelected(difficulty, level)
            )
        );
        return new StageScreen(spriteBatch, levelSelectionView);
    }

    public Screen sessionScreen(
        String difficulty,
        int level
    ) {
        var world = configuration.world();
        var viewport = new FitViewport(world.width(), world.height());
        var physicsWorld = sessionFactory.world();
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
            new StageScreen(
                spriteBatch,
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
                        physicsWorld
                    ),
                    assetManager
                )
            ),
            physicsWorld
        );
    }
}
