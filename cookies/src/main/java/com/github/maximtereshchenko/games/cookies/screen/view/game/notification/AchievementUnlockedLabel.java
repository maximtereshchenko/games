package com.github.maximtereshchenko.games.cookies.screen.view.game.notification;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

final class AchievementUnlockedLabel extends Label {

    AchievementUnlockedLabel(Skin skin, I18NBundle bundle) {
        super(
            bundle.get("notifications.achievement.unlocked"),
            skin,
            "achievement-unlocked"
        );
    }
}
