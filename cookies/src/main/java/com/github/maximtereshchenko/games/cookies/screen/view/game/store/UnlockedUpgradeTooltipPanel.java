package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.game.UpgradeTooltipPanel;

import java.util.Optional;

final class UnlockedUpgradeTooltipPanel extends UpgradeTooltipPanel {

    UnlockedUpgradeTooltipPanel(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        super(assetManager, assets, bigDecimalFormatter, bakeryService, upgrade);
    }

    @Override
    protected Label price(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        return new UpgradePriceLabel(
            assetManager,
            assets,
            bigDecimalFormatter,
            bakeryService,
            upgrade
        );
    }

    @Override
    protected Optional<Actor> footer(
        AssetManager assetManager,
        Assets assets
    ) {
        return Optional.of(
            new UpgradeTooltipFooterLabel(
                assetManager,
                assets
            )
        );
    }
}
