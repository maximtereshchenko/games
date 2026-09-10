package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;

final class ActiveUpgradePanel extends Table {

    ActiveUpgradePanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService
    ) {
        add(
            new StatisticsLine(
                skin,
                bundle.get("statistics.upgrades.active"),
                new UpgradeStatisticsLabel(
                    skin,
                    bundle,
                    bakeryService
                )
            )
        )
            .left()
            .padBottom(4)
            .row();
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
