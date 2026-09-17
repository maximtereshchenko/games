package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.display.BuildingDisplayPanel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.statistics.StatisticsPanel;

import java.time.Clock;
import java.util.Random;

final class MainPanel extends Stack {

    private final AssetManager assetManager;
    private final Assets assets;
    private final BuildingDisplayPanel buildingDisplayPanel;
    private final StatisticsPanel statisticsPanel;
    private Actor current;

    MainPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random,
        Clock clock
    ) {
        this.assetManager = assetManager;
        this.assets = assets;
        this.buildingDisplayPanel = new BuildingDisplayPanel(
            assetManager,
            assets,
            bakeryService,
            random
        );
        this.statisticsPanel = new StatisticsPanel(
            assetManager,
            assets,
            bigDecimalFormatter,
            bakeryService,
            clock
        );
        this.current = buildingDisplayPanel;
        add(buildingDisplayPanel);
    }

    void toggle() {
        clearChildren();
        current = next();
        add(current);
        assetManager.get(sound()).play();
    }

    private AssetDescriptor<Sound> sound() {
        var gameAssets = assets.game();
        if (current == buildingDisplayPanel) {
            return gameAssets.menuOffSound();
        }
        return gameAssets.menuOnSound();
    }

    private Actor next() {
        if (current == buildingDisplayPanel) {
            return statisticsPanel;
        }
        return buildingDisplayPanel;
    }
}
