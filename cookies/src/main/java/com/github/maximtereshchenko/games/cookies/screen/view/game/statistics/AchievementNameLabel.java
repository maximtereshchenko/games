package com.github.maximtereshchenko.games.cookies.screen.view.game.statistics;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

final class AchievementNameLabel extends Label {

    AchievementNameLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        super(
            bundle.get("statistics.achievement.tooltip.locked.name"),
            skin,
            "achievement-name"
        );
        addAction(
            new UnlockAchievementAction(
                bakeryService,
                achievement,
                Actions.run(
                    () -> setText(
                        bundle.get(
                            "statistics.achievement.%s.name"
                                .formatted(achievement.name())
                        )
                    )
                )
            )
        );
    }
}
