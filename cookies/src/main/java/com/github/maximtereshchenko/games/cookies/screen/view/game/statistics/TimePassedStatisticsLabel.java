package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

import java.time.Duration;

final class TimePassedStatisticsLabel extends StatisticsValueLabel {

    private final I18NBundle bundle;

    TimePassedStatisticsLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        super(skin, bakeryService);
        this.bundle = bundle;
    }

    @Override
    String text(BakeryService bakeryService) {
        var duration = Duration.between(
            bakeryService.createdTimestamp(),
            bakeryService.lastUpdatedTimestamp()
        );
        return bundle.format(
            "statistics.general.time-passed.value",
            duration.toHours(),
            duration.toMinutesPart(),
            duration.toSecondsPart()
        );
    }
}
