package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Align;

import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.AchievementTooltipPanel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;
import com.github.maximtereshchenko.games.cookies.screen.view.game.TopCenterTooltipWidget;

final class AchievementIconPanel extends HorizontalGroup {

    AchievementIconPanel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService
    ) {
        wrap();
        rowAlign(Align.left);
        for (var achievement : Achievement.values()) {
            var achievementIcon = new HoverableAchievementIcon(
                assetManager,
                assets,
                bakeryService,
                achievement
            );
            achievementIcon.addListener(
                new TopCenterTooltipWidget(
                    new PopUpFrame(
                        assetManager,
                        assets,
                        new AchievementTooltipPanel(
                            assetManager,
                            assets,
                            bakeryService,
                            achievement
                        )
                    )
                )
            );
            addActor(achievementIcon);
        }
    }
}
