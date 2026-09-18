package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.display.BuildingDisplayPanel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.options.OptionsPanel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.statistics.StatisticsPanel;

import java.time.Clock;
import java.util.Random;

final class MainPanel extends Stack {

    private final AssetManager assetManager;
    private final Assets assets;
    private final BakeryService bakeryService;
    private final BuildingDisplayPanel buildingDisplayPanel;
    private final TogglablePanel statisticsPanel;
    private final TogglablePanel optionsPanel;
    private Actor current;

    MainPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random,
        Clock clock
    ) {
        var bundle = assetManager.get(
            assets.game()
                .bundle()
        );
        this.assetManager = assetManager;
        this.assets = assets;
        this.bakeryService = bakeryService;
        this.buildingDisplayPanel = new BuildingDisplayPanel(
            assetManager,
            assets,
            bakeryService,
            random
        );
        this.statisticsPanel = new TogglablePanel(
            assetManager,
            assets,
            bundle.get("statistics.title"),
            new StatisticsPanel(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService,
                clock
            )
        );
        this.optionsPanel = new TogglablePanel(
            assetManager,
            assets,
            bundle.get("options.title"),
            new OptionsPanel(
                assetManager,
                assets,
                bakeryService
            )
        );
        this.current = buildingDisplayPanel;
        add(buildingDisplayPanel);
    }

    void toggleStatistics() {
        toggle(statisticsPanel);
    }

    void toggleOptions() {
        toggle(optionsPanel);
    }

    private void toggle(Actor requested) {
        clearChildren();
        current = next(requested);
        add(current);
        assetManager.get(sound())
            .play(bakeryService.volume());
    }

    private AssetDescriptor<Sound> sound() {
        var gameAssets = assets.game();
        if (current == buildingDisplayPanel) {
            return gameAssets.menuOffSound();
        }
        return gameAssets.menuOnSound();
    }

    private Actor next(Actor requested) {
        if (current == requested) {
            return buildingDisplayPanel;
        }
        return requested;
    }
}
