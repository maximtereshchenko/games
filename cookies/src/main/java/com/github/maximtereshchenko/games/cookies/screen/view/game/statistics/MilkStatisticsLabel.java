package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class MilkStatisticsLabel extends StatisticsValueLabel {

    private final I18NBundle bundle;

    MilkStatisticsLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        super(skin, bakeryService);
        this.bundle = bundle;
    }

    @Override
    String text(BakeryService bakeryService) {
        return bundle.format(
            "statistics.achievements.milk.value",
            bakeryService.milk()
        );
    }
}
