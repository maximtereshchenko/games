package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.UpgradeTooltipPanel;

import java.util.Optional;

final class UnlockedUpgradeTooltipPanel extends UpgradeTooltipPanel {

    UnlockedUpgradeTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        super(skin, bundle, bigDecimalFormatter, bakeryService, upgrade);
    }

    @Override
    protected Label price(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        return new UpgradePriceLabel(
            skin,
            bigDecimalFormatter,
            bakeryService,
            upgrade
        );
    }

    @Override
    protected Optional<Actor> footer(
        Skin skin,
        I18NBundle bundle
    ) {
        return Optional.of(
            new UpgradeTooltipFooterLabel(
                skin,
                bundle
            )
        );
    }
}
