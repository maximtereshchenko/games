package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Upgrade;
import com.github.maximtereshchenko.games.cookies.screen.BigDecimalFormatter;

final class UpgradePriceLabel extends Label {

    private final Style style;
    private final BakeryService bakeryService;
    private final Upgrade upgrade;

    UpgradePriceLabel(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Upgrade upgrade
    ) {
        var labelStyle = skin.get(Style.class);
        super("", labelStyle.labelStyle(bakeryService, upgrade));
        this.style = labelStyle;
        this.bakeryService = bakeryService;
        this.upgrade = upgrade;
        setText(
            bigDecimalFormatter.string(
                bakeryService.price(upgrade)
            )
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setStyle(style.labelStyle(bakeryService, upgrade));
    }

    private static final class Style {

        private BitmapFont font;
        private Color enabledFontColor;
        private Color disabledFontColor;

        LabelStyle labelStyle(
            BakeryService bakeryService,
            Upgrade upgrade
        ) {
            return new LabelStyle(
                font,
                color(bakeryService, upgrade)
            );
        }

        private Color color(
            BakeryService bakeryService,
            Upgrade upgrade
        ) {
            if (bakeryService.canAfford(upgrade)) {
                return enabledFontColor;
            }
            return disabledFontColor;
        }
    }
}
