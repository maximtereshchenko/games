package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;

import java.math.BigDecimal;

final class TransactionValueLabel extends Label {

    private final Style style;
    private final BigDecimalFormatter bigDecimalFormatter;
    private final Transaction transaction;
    private final Building building;

    TransactionValueLabel(
        Skin skin,
        BigDecimalFormatter bigDecimalFormatter,
        Transaction transaction,
        Building building
    ) {
        var labelStyle = skin.get(Style.class);
        super(
            "",
            new LabelStyle(
                labelStyle.font,
                labelStyle.disabledFontColor
            )
        );
        this.bigDecimalFormatter = bigDecimalFormatter;
        this.style = labelStyle;
        this.transaction = transaction;
        this.building = building;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setText(
            bigDecimalFormatter.string(
                transaction.value(
                    building
                )
            )
        );
        setStyle(new LabelStyle(style.font, color()));
    }

    private Color color() {
        if (
            transaction.canAfford(building) &&
            transaction.value(building).compareTo(BigDecimal.ZERO) > 0
        ) {
            return style.enabledFontColor;
        }
        return style.disabledFontColor;
    }

    private static final class Style {

        BitmapFont font;
        Color enabledFontColor;
        Color disabledFontColor;
    }
}
