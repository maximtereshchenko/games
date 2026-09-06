package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;

final class UpgradeTooltipPanel extends Table {

    UpgradeTooltipPanel(
        Skin skin,
        I18NBundle bundle,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        var style = skin.get(upgrade.name(), Style.class);
        defaults().padBottom(8);
        add(new Image(style.upgradeIcon))
            .width(Value.prefWidth);
        add(
            new UpgradeTooltipHeader(
                skin,
                bundle,
                bakeryService,
                upgrade
            )
        )
            .growX()
            .row();
        addSeparator(style);
        add(
            new UpgradeDescriptionLabel(
                skin,
                bundle,
                upgrade
            )
        )
            .colspan(2)
            .left()
            .row();
        add(
            new UpgradeFlavorTextLabel(
                skin,
                bundle,
                upgrade
            )
        )
            .colspan(2)
            .right()
            .row();
        addSeparator(style);
        add(new UpgradeTooltipFooterLabel(skin, bundle))
            .colspan(2);
    }

    private void addSeparator(Style style) {
        add(new Image(style.separator))
            .colspan(2)
            .growX()
            .padTop(8)
            .padBottom(16)
            .row();
    }

    private static final class Style {

        Drawable upgradeIcon;
        Drawable separator;
    }
}
