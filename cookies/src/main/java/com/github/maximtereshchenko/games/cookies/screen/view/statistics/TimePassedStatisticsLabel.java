package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

final class TimePassedStatisticsLabel extends StatisticsValueLabel {

    private final I18NBundle bundle;
    private final Clock clock;

    TimePassedStatisticsLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Clock clock
    ) {
        super(skin, bakeryService);
        this.bundle = bundle;
        this.clock = clock;
    }

    @Override
    String text(BakeryService bakeryService) {
        var duration = Duration.between(
            bakeryService.timestamp(),
            Instant.now(clock)
        );
        return bundle.format(
            "statistics.time-passed.format",
            duration.toHours(),
            duration.toMinutesPart(),
            duration.toSecondsPart()
        );
    }
}
