package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class CumulativeClicksStatisticsLabel extends StatisticsValueLabel {

    CumulativeClicksStatisticsLabel(
        Skin skin,
        BakeryService bakeryService
    ) {
        super(skin, bakeryService);
    }

    @Override
    String text(BakeryService bakeryService) {
        return String.valueOf(bakeryService.cumulativeClicks());
    }
}
