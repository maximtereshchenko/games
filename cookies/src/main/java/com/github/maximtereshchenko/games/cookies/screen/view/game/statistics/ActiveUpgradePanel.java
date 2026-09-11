package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

final class ActiveUpgradePanel extends StatisticLinePanel {

    ActiveUpgradePanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        super(
            new StatisticsLine(
                skin,
                bundle.get("statistics.upgrades.active"),
                new UpgradeStatisticsLabel(
                    skin,
                    bundle,
                    bakeryService
                )
            )
        );
        add(
            new ActiveUpgradeIconPanel(
                skin,
                bundle,
                bigDecimalFormatter,
                bakeryService
            )
        )
            .growX();
    }
}
