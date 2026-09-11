package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

final class UpgradeStatisticsLabel extends PercentStatisticsLabel<Upgrade> {

    UpgradeStatisticsLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        super(
            skin,
            bundle,
            "statistics.upgrades.active.value",
            bakeryService,
            Upgrade.values()
        );
    }

    @Override
    boolean isCounted(BakeryService bakeryService, Upgrade value) {
        return bakeryService.isActive(value);
    }
}
