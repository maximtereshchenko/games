package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class AchievementPanel extends StatisticLinePanel {

    AchievementPanel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        super(
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.achievements.unlocked"),
                new AchievementStatisticsLabel(
                    assetManager,
                    assets,
                    bakeryService
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.achievements.milk"),
                new MilkStatisticsLabel(
                    assetManager,
                    assets,
                    bakeryService
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.achievements.kitten-multiplier"),
                new KittenMultiplierStatisticsLabel(
                    assetManager,
                    assets,
                    bakeryService
                )
            )
        );
        add(
            new AchievementIconPanel(
                assetManager,
                assets,
                bakeryService
            )
        )
            .growX();
    }
}
