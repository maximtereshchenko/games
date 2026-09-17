package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

final class TimePassedStatisticsLabel extends StatisticsValueLabel {

    private final I18NBundle bundle;
    private final Clock clock;

    TimePassedStatisticsLabel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Clock clock
    ) {
        super(assetManager, assets, bakeryService);
        this.bundle = assetManager.get(assets.game().bundle());
        this.clock = clock;
    }

    @Override
    String text(BakeryService bakeryService) {
        var duration = Duration.between(
            bakeryService.createdTimestamp(),
            Instant.now(clock)
        );
        return bundle.format(
            "statistics.general.time-passed.value",
            duration.toHours(),
            duration.toMinutesPart(),
            duration.toSecondsPart()
        );
    }
}
