package com.github.maximtereshchenko.games.cookies.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.maximtereshchenko.games.common.screen.ScreenLayout;
import com.github.maximtereshchenko.games.common.screen.StageScreen;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.PlayerProgress;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BakeryView;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.loading.LoadingView;

import java.time.Clock;
import java.util.concurrent.ThreadLocalRandom;

public final class ScreenFactory {

    private final SpriteBatch spriteBatch;
    private final AssetManager assetManager;
    private final Assets assets;
    private final Game game;

    public ScreenFactory(
        SpriteBatch spriteBatch,
        AssetManager assetManager,
        Assets assets,
        Game game
    ) {
        this.spriteBatch = spriteBatch;
        this.assetManager = assetManager;
        this.assets = assets;
        this.game = game;
    }

    public Screen loadingScreen() {
        var loadingAssets = assets.loading();
        loadingAssets.all().forEach(assetManager::load);
        assetManager.finishLoading();
        assets.game().all().forEach(assetManager::load);
        return new LoadingScreen(
            stageScreen(
                new LoadingView(
                    assetManager.get(loadingAssets.skin()),
                    assetManager.get(loadingAssets.bundle())
                )
            ),
            assetManager,
            this,
            game
        );
    }

    Screen bakeryScreen() {
        var gameAssets = assets.game();
        var clock = Clock.systemDefaultZone();
        var playerProgress = new PlayerProgress(
            assetManager.get(
                gameAssets.preferences()
            ),
            clock
        );
        var bakeryService = new BakeryService(
            assetManager.get(
                gameAssets.configuration()
            ),
            playerProgress,
            clock
        );
        return new BakeryScreen(
            stageScreen(
                new BakeryView(
                    assetManager.get(
                        gameAssets.skin()
                    ),
                    assetManager.get(
                        gameAssets.bundle()
                    ),
                    new BigDecimalFormatter(),
                    bakeryService,
                    ThreadLocalRandom.current()
                )
            ),
            bakeryService,
            playerProgress
        );
    }

    private Screen stageScreen(ScreenLayout screenLayout) {
        var stage = new Stage(new ScreenViewport(), spriteBatch);
        stage.addActor(screenLayout);
        return new StageScreen(stage);
    }
}
