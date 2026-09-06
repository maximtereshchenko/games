package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class TransactionValueLabel extends Label {

    private final Style style;
    private final BakeryService bakeryService;
    private final Building building;

    TransactionValueLabel(
        Skin skin,
        BakeryService bakeryService,
        Building building
    ) {
        var labelStyle = skin.get(Style.class);
        super(
            "",
            labelStyle.labelStyle(bakeryService, building)
        );
        this.style = labelStyle;
        this.bakeryService = bakeryService;
        this.building = building;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(
            bakeryService.transactionValue(building)
                .toString()
        );
        setStyle(style.labelStyle(bakeryService, building));
    }

    private static final class Style {

        private BitmapFont font;
        private Color enabledFontColor;
        private Color disabledFontColor;

        LabelStyle labelStyle(
            BakeryService bakeryService,
            Building building
        ) {
            return new LabelStyle(font, color(bakeryService, building));
        }

        private Color color(
            BakeryService bakeryService,
            Building building
        ) {
            if (bakeryService.canAfford(building)) {
                return enabledFontColor;
            }
            return disabledFontColor;
        }
    }
}
