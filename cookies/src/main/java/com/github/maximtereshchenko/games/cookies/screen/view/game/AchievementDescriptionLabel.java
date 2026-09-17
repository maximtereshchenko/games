package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class AchievementDescriptionLabel extends Label {

    AchievementDescriptionLabel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        super(
            bundle.get("achievement.tooltip.locked.description"),
            assetManager.get(assets.game().skin()),
            "achievement-description"
        );
        setWrap(true);
        addAction(
            new UnlockAchievementAction(
                bakeryService,
                achievement,
                Actions.run(
                    () -> setText(
                        bundle.get(
                            "achievement.%s.description"
                                .formatted(achievement.name())
                        )
                    )
                )
            )
        );
    }
}
