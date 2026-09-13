package com.github.maximtereshchenko.games.cookies.screen.view.game.notification;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.view.game.AchievementIcon;
import com.github.maximtereshchenko.games.cookies.screen.view.game.AchievementNameLabel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpSeparator;

final class NotificationPanel extends Table {

    NotificationPanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        add(
            new AchievementIcon(
                skin,
                bakeryService,
                achievement
            )
        )
            .width(Value.prefWidth)
            .height(Value.prefHeight)
            .padRight(6);
        add(
            table(
                skin,
                bundle,
                bakeryService,
                achievement
            )
        )
            .growX();
        add(new CloseButton(skin))
            .top()
            .right();
    }

    private Table table(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        var achievementNameLabel = new AchievementNameLabel(
            skin,
            bundle,
            bakeryService,
            achievement
        );
        achievementNameLabel.setWrap(true);
        var table = new Table();
        table.add(
                new AchievementUnlockedLabel(
                    skin,
                    bundle
                )
            )
            .padBottom(8)
            .left()
            .row();
        table.add(new PopUpSeparator(skin))
            .padBottom(4)
            .growX()
            .row();
        table.add(achievementNameLabel)
            .growX();
        return table;
    }
}
