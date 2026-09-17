package com.github.maximtereshchenko.games.cookies.screen.view.game.notification;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import com.github.maximtereshchenko.games.cookies.domain.Achievement;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.AchievementIcon;
import com.github.maximtereshchenko.games.cookies.screen.view.game.AchievementNameLabel;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpSeparator;

final class NotificationPanel extends Table {

    NotificationPanel(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        add(
            new AchievementIcon(
                assetManager,
                assets,
                bakeryService,
                achievement
            )
        )
            .width(Value.prefWidth)
            .height(Value.prefHeight)
            .padRight(6);
        add(
            table(
                assetManager,
                assets,
                bakeryService,
                achievement
            )
        )
            .growX();
        add(new CloseButton(assetManager, assets))
            .top()
            .right();
    }

    private Table table(
        AssetManager assetManager,
        Assets assets,
        BakeryService bakeryService,
        Achievement achievement
    ) {
        var achievementNameLabel = new AchievementNameLabel(
            assetManager,
            assets,
            bakeryService,
            achievement
        );
        achievementNameLabel.setWrap(true);
        var table = new Table();
        table.add(
                new AchievementUnlockedLabel(
                    assetManager,
                    assets
                )
            )
            .padBottom(8)
            .left()
            .row();
        table.add(new PopUpSeparator(assetManager, assets))
            .padBottom(4)
            .growX()
            .row();
        table.add(achievementNameLabel)
            .growX();
        return table;
    }
}
