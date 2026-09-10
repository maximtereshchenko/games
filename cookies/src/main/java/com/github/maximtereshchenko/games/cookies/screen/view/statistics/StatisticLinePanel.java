package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

abstract class StatisticLinePanel extends Table {

    StatisticLinePanel(StatisticsLine... lines) {
        left();
        defaults().left().padBottom(4);
        for (var line : lines) {
            add(line).row();
        }
    }
}
