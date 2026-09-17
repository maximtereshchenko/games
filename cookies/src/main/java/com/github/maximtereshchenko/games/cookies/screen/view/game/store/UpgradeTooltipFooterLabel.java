package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import com.github.maximtereshchenko.games.cookies.screen.Assets;

final class UpgradeTooltipFooterLabel extends Label {

    UpgradeTooltipFooterLabel(AssetManager assetManager, Assets assets) {
        super(
            assetManager.get(assets.game().bundle()).get("upgrade.tooltip.purchase-hint"),
            assetManager.get(assets.game().skin()),
            "upgrade-tooltip-footer"
        );
        setAlignment(Align.center);
    }
}
