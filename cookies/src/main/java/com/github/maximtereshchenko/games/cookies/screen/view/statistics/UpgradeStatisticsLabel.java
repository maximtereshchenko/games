package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

import java.util.stream.Stream;

final class UpgradeStatisticsLabel extends StatisticsValueLabel {

    private final I18NBundle bundle;

    UpgradeStatisticsLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        super(skin, bakeryService);
        this.bundle = bundle;
    }

    @Override
    String text(BakeryService bakeryService) {
        var upgrades = Upgrade.values();
        var active = Stream.of(upgrades)
            .filter(bakeryService::isActive)
            .count();
        return bundle.format(
            "statistics.upgrades.active.value",
            active,
            upgrades.length,
            (float) active / upgrades.length
        );
    }
}
