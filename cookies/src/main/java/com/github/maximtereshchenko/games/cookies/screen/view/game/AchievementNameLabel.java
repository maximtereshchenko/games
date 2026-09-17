package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

public final class AchievementNameLabel extends Label {

    public AchievementNameLabel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        var bundle = assetManager.get(assets.game().bundle());
        super(
            bundle.get("achievement.locked.name"),
            assetManager.get(assets.game().skin()),
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
