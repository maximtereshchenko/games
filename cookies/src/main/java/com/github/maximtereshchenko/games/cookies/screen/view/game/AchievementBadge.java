package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class AchievementBadge extends Badge {

    AchievementBadge(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        super(
            assetManager,
            assets,
            bundle.get("achievement.tooltip.badge.locked")
        );
        addAction(
            new UnlockAchievementAction(
                bakeryService,
                achievement,
                Actions.run(
                    () -> setText(
                        bundle.get(
                            "achievement.tooltip.badge.unlocked"
                        )
                    )
                )
            )
        );
    }
}
