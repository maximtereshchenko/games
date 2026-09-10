package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.SmallCookieIcon;

final class UpgradeTooltipHeader extends Table {

    UpgradeTooltipHeader(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        add(
            new UpgradeNameLabel(
                skin,
                bundle,
                upgrade
            )
        )
            .left();
        add().growX();
        add(new SmallCookieIcon(skin))
            .width(Value.prefWidth);
        add(
            new UpgradePriceLabel(
                skin,
                bigDecimalFormatter,
                bakeryService,
                upgrade
            )
        )
            .row();
        add(new UpgradeLabel(skin, bundle)).left();
    }
}
