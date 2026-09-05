package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

final class UpgradeTooltipFooterLabel extends Label {

    UpgradeTooltipFooterLabel(Skin skin, I18NBundle bundle) {
        super(
            bundle.get("upgrades.tooltips.clickToPurchase"),
            skin,
            "label_upgradeTooltipFooter"
        );
    }
}
