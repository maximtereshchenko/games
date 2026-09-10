package com.github.maximtereshchenko.games.cookies.screen.view.statistics;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.Badge;

final class AchievementBadge extends Badge {

    AchievementBadge(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        super(
            skin,
            bundle.get("statistics.achievement.tooltip.badge.locked")
        );
        addAction(
            new UnlockAchievementAction(
                bakeryService,
                achievement,
                Actions.run(
                    () -> setText(
                        bundle.get(
                            "statistics.achievement.tooltip.badge.unlocked"
                        )
                    )
                )
            )
        );
    }
}
