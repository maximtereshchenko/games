package com.github.maximtereshchenko.games.cookies.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.maximtereshchenko.games.common.screen.StageScreen;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.PlayerProgress;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BakeryView;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.loading.LoadingView;

import java.time.Clock;
import java.util.Random;

public final class ScreenFactory {

    private final SpriteBatch spriteBatch;
    private final AssetManager assetManager;
    private final Assets assets;
    private final Game game;
    private final Random random;

    public ScreenFactory(
        SpriteBatch spriteBatch,
        AssetManager assetManager,
        Assets assets,
        Game game,
        Random random
    ) {
        this.spriteBatch = spriteBatch;
        this.assetManager = assetManager;
        this.assets = assets;
        this.game = game;
        this.random = random;
    }

    public Screen loadingScreen() {
        var loadingAssets = assets.loading();
        loadingAssets.all().forEach(assetManager::load);
        assetManager.finishLoading();
        assets.game().all().forEach(assetManager::load);
        return new LoadingScreen(
            stageScreen(
                new LoadingView(
                    assetManager,
                    assets
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
        var bakeryService = new BakeryService(
            assetManager.get(
                gameAssets.configuration()
            ),
            new PlayerProgress(
                assetManager.get(
                    gameAssets.preferences()
                ),
                clock
            ),
            clock,
            random
        );
        return new BakeryScreen(
            stageScreen(
                new BakeryView(
                    assetManager,
                    assets,
                    new BigDecimalFormatter(),
                    bakeryService,
                    random,
                    clock
                )
            ),
            bakeryService
        );
    }

    private Screen stageScreen(WidgetGroup widgetGroup) {
        widgetGroup.setFillParent(true);
        var stage = new Stage(new ScreenViewport(), spriteBatch);
        stage.addActor(widgetGroup);
        return new StageScreen(stage);
    }
}
