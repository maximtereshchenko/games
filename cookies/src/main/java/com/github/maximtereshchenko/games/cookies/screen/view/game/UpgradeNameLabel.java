package com.github.maximtereshchenko.games.cookies.screen.view.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class UpgradeNameLabel extends Label {

    UpgradeNameLabel(AssetManager assetManager, Assets assets, Upgrade upgrade) {
        super(
            assetManager.get(assets.game().bundle()).get("store.upgrade.%s.name".formatted(upgrade.name())),
            assetManager.get(assets.game().skin()),
            "upgrade-name"
        );
    }
}
