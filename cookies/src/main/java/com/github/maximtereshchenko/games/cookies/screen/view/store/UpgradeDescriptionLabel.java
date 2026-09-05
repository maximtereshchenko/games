package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

final class UpgradeDescriptionLabel extends Label {

    UpgradeDescriptionLabel(Skin skin, I18NBundle bundle, Upgrade upgrade) {
        super(
            bundle.get("upgrades.descriptions." + upgrade.name()),
            skin,
            "label_upgradeDescription"
        );
    }
}
