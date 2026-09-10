package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class AchievementIconPanel extends HorizontalGroup {

    AchievementIconPanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService
    ) {
        wrap();
        rowAlign(Align.left);
        for (var achievement : Achievement.values()) {
            var achievementIcon = new AchievementIcon(
                skin,
                bakeryService,
                achievement
            );
            achievementIcon.addListener(
                new StatisticsTooltipWidget(
                    skin,
                    new AchievementTooltipPanel(
                        skin,
                        bundle,
                        bakeryService,
                        achievement
                    )
                )
            );
            addActor(achievementIcon);
        }
    }
}
