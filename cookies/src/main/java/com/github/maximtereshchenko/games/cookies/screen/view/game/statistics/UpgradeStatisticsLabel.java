package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class UpgradeStatisticsLabel extends PercentStatisticsLabel<Upgrade> {

    UpgradeStatisticsLabel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        super(
            assetManager,
            assets,
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
