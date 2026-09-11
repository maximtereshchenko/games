package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

final class UpgradeTooltipFooterLabel extends Label {

    UpgradeTooltipFooterLabel(Skin skin, I18NBundle bundle) {
        super(
            bundle.get("upgrade.tooltip.purchase-hint"),
            skin,
            "upgrade-tooltip-footer"
        );
        setAlignment(Align.center);
    }
}
