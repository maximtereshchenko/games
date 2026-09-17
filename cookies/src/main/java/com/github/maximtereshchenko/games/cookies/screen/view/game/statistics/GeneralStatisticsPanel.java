package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.game.SmallCookieIcon;

import java.time.Clock;

final class GeneralStatisticsPanel extends StatisticLinePanel {

    GeneralStatisticsPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Clock clock
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        super(
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.general.balance"),
                new SmallCookieIcon(assetManager, assets),
                new BalanceStatisticsLabel(
                    assetManager,
                    assets,
                    bigDecimalFormatter,
                    bakeryService
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.general.cumulative-baked"),
                new SmallCookieIcon(assetManager, assets),
                new CumulativeBakedStatisticsLabel(
                    assetManager,
                    assets,
                    bigDecimalFormatter,
                    bakeryService
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.general.time-passed"),
                new TimePassedStatisticsLabel(
                    assetManager,
                    assets,
                    bakeryService,
                    clock
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.general.buildings-count"),
                new BuildingsCountStatisticsLabel(
                    assetManager,
                    assets,
                    bakeryService
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.general.baking-rate"),
                new SmallCookieIcon(assetManager, assets),
                new BakingRateStatisticsLabel(
                    assetManager,
                    assets,
                    bigDecimalFormatter,
                    bakeryService
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.general.baking-power"),
                new SmallCookieIcon(assetManager, assets),
                new BakingPowerStatisticsLabel(
                    assetManager,
                    assets,
                    bigDecimalFormatter,
                    bakeryService
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.general.cumulative-clicks"),
                new CumulativeClicksStatisticsLabel(
                    assetManager,
                    assets,
                    bakeryService
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.general.cumulative-manually-baked"),
                new SmallCookieIcon(assetManager, assets),
                new CumulativeManuallyBakedStatisticsLabel(
                    assetManager,
                    assets,
                    bigDecimalFormatter,
                    bakeryService
                )
            ),
            new StatisticsLine(
                assetManager,
                assets,
                bundle.get("statistics.general.cumulative-golden-cookies"),
                new CumulativeGoldenCookiesStatisticsLabel(
                    assetManager,
                    assets,
                    bakeryService
                )
            )
        );
    }
}
