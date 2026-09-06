package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

final class UpgradeLabel extends Label {

    UpgradeLabel(Skin skin, I18NBundle bundle) {
        super(
            bundle.get("store.upgrade.badge"),
            skin,
            "upgrade"
        );
    }
}
