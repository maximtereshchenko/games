package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class AchievementPanel extends StatisticLinePanel {

    AchievementPanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        super(
            new StatisticsLine(
                skin,
                bundle.get("statistics.achievements.unlocked"),
                new UpgradeStatisticsLabel(
                    skin,
                    bundle,
                    bakeryService
                )
            )
        );
        add(
            new AchievementIconPanel(
                skin,
                bakeryService
            )
        )
            .growX();
    }
}
