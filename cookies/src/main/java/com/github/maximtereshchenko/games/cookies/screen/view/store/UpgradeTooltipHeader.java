package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

final class UpgradeTooltipHeader extends Table {

    UpgradeTooltipHeader(
        Skin skin,
        I18NBundle bundle,
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
        add(new Image(skin.get(Style.class).icon))
            .width(Value.prefWidth);
        add(
            new UpgradePriceLabel(
                skin,
                bakeryService,
                upgrade
            )
        )
            .row();
        add(new UpgradeLabel(skin, bundle)).left();
    }

    private static final class Style {

        Drawable icon;
    }
}
