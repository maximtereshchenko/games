package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class ActiveUpgradePanel extends StatisticLinePanel {

    ActiveUpgradePanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(
            new StatisticsLine(
                assetManager,
                assets,
                assetManager.get(assets.game().bundle()).get("statistics.upgrades.active"),
                new UpgradeStatisticsLabel(
                    assetManager,
                    assets,
                    bakeryService
                )
            )
        );
        add(
            new ActiveUpgradeIconPanel(
                assetManager,
                assets,
                bigDecimalFormatter,
                bakeryService
            )
        )
            .growX();
    }
}
