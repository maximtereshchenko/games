package com.github.maximtereshchenko.games.cookies.screen.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;

final class NavigationPanel extends Table {

    NavigationPanel(Skin skin, I18NBundle bundle) {
        add(
            new TextButton(
                bundle.get("navigation.statistics"),
                skin,
                "left-bottom"
            )
        );
        add().growX();
        add(
            new TextButton(
                bundle.get("navigation.legacy"),
                skin,
                "right-bottom"
            )
        );
    }
}
