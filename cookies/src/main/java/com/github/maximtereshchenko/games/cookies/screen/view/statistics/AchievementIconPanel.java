package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class AchievementIconPanel extends HorizontalGroup {

    AchievementIconPanel(
        Skin skin,
        BakeryService bakeryService
    ) {
        wrap();
        rowAlign(Align.left);
        for (var achievement : Achievement.values()) {
            addActor(
                new AchievementIcon(
                    skin,
                    bakeryService,
                    achievement
                )
            );
        }
    }
}
