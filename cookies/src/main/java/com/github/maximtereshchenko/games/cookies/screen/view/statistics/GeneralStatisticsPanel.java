package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.SmallCookieIcon;

import java.time.Clock;

final class GeneralStatisticsPanel extends Table {

    GeneralStatisticsPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Clock clock
    ) {
        left();
        defaults().left().padBottom(4);
        add(
            new StatisticsLine(
                skin,
                bundle.get("statistics.general.balance"),
                new SmallCookieIcon(skin),
                new BalanceStatisticsLabel(
                    skin,
                    bigDecimalFormatter,
                    bakeryService
                )
            )
        )
            .row();
        add(
            new StatisticsLine(
                skin,
                bundle.get("statistics.general.cumulative-baked"),
                new SmallCookieIcon(skin),
                new CumulativeBakedStatisticsLabel(
                    skin,
                    bigDecimalFormatter,
                    bakeryService
                )
            )
        )
            .row();
        add(
            new StatisticsLine(
                skin,
                bundle.get("statistics.general.time-passed"),
                new TimePassedStatisticsLabel(
                    skin,
                    bundle,
                    bakeryService,
                    clock
                )
            )
        )
            .row();
        add(
            new StatisticsLine(
                skin,
                bundle.get("statistics.general.buildings-count"),
                new BuildingsCountStatisticsLabel(
                    skin,
                    bakeryService
                )
            )
        )
            .row();
        add(
            new StatisticsLine(
                skin,
                bundle.get("statistics.general.baking-rate"),
                new SmallCookieIcon(skin),
                new BakingRateStatisticsLabel(
                    skin,
                    bigDecimalFormatter,
                    bakeryService
                )
            )
        )
            .row();
        add(
            new StatisticsLine(
                skin,
                bundle.get("statistics.general.baking-power"),
                new SmallCookieIcon(skin),
                new BakingPowerStatisticsLabel(
                    skin,
                    bigDecimalFormatter,
                    bakeryService
                )
            )
        )
            .row();
        add(
            new StatisticsLine(
                skin,
                bundle.get("statistics.general.cumulative-clicks"),
                new CumulativeClicksStatisticsLabel(
                    skin,
                    bakeryService
                )
            )
        )
            .row();
        add(
            new StatisticsLine(
                skin,
                bundle.get("statistics.general.cumulative-manually-baked"),
                new SmallCookieIcon(skin),
                new CumulativeManuallyBakedStatisticsLabel(
                    skin,
                    bigDecimalFormatter,
                    bakeryService
                )
            )
        )
            .row();
    }
}
