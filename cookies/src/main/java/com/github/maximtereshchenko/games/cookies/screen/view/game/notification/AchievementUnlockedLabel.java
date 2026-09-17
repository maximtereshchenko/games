package com.github.maximtereshchenko.games.cookies.screen.view.game.notification;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class AchievementUnlockedLabel extends Label {

    AchievementUnlockedLabel(AssetManager assetManager, Assets assets) {
        super(
            assetManager.get(assets.game().bundle()).get("notifications.achievement.unlocked"),
            assetManager.get(assets.game().skin()),
            "achievement-unlocked"
        );
    }
}
