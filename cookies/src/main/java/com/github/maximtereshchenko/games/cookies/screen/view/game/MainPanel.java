package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.display.BuildingDisplayPanel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.statistics.StatisticsPanel;

import java.time.Clock;
import java.util.Random;

final class MainPanel extends Stack {

    private final BuildingDisplayPanel buildingDisplayPanel;
    private final StatisticsPanel statisticsPanel;
    private Actor current;

    MainPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Random random,
        Clock clock
    ) {
        this.buildingDisplayPanel = new BuildingDisplayPanel(
            skin,
            bakeryService,
            random
        );
        this.statisticsPanel = new StatisticsPanel(
            skin,
            bundle,
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
    }

    private Actor next() {
        if (current == buildingDisplayPanel) {
            return statisticsPanel;
        }
        return buildingDisplayPanel;
    }
}
