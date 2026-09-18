package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class NavigationPanel extends Table {

    NavigationPanel(
        AssetManager assetManager,
        Assets assets,
        EventListener statisticsEventListener,
        EventListener optionsEventListener
    ) {
        var skin = assetManager.get(assets.game().skin());
        var bundle = assetManager.get(assets.game().bundle());
        var statisticsButton = new TextButton(
            bundle.get("navigation.statistics"),
            skin,
            "left-bottom"
        );
        statisticsButton.addListener(statisticsEventListener);
        var optionsButton = new TextButton(
            bundle.get("navigation.options"),
            skin,
            "right-bottom"
        );
        optionsButton.addListener(optionsEventListener);
        add(statisticsButton);
        add().growX();
        add(optionsButton);
    }
}
