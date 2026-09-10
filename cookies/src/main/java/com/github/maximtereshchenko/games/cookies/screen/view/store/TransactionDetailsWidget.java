package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.SmallCookieIcon;

final class TransactionDetailsWidget extends Table {

    TransactionDetailsWidget(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        BakeryService bakeryService,
        Building building
    ) {
        add(
            new BuildingNameLabel(
                skin,
                "building-name-button",
                bundle,
                bakeryService,
                building
            )
        )
            .colspan(2)
            .left()
            .padBottom(4)
            .row();
        add(new SmallCookieIcon(skin))
            .width(Value.prefWidth)
            .padTop(2);
        add(
            new TransactionValueLabel(
                skin,
                bigDecimalFormatter,
                bakeryService,
                building
            )
        )
            .expandX()
            .left();
    }
}
