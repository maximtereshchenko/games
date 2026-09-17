package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

public final class AchievementNameLabel extends Label {

    public AchievementNameLabel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        super(
            bundle.get("achievement.locked.name"),
            skin,
            "achievement-name"
        );
        setWrap(true);
        addAction(
            new UnlockAchievementAction(
                bakeryService,
                achievement,
                Actions.run(
                    () -> setText(
                        bundle.get(
                            "achievement.%s.name"
                                .formatted(achievement.name())
                        )
                    )
                )
            )
        );
    }
}
