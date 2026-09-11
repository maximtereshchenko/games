package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.game.SmallCookieIcon;

final class TransactionDetailsWidget extends Table {

    TransactionDetailsWidget(
        Skin skin,
        I18NBundle bundle,
        BigDecimalFormatter bigDecimalFormatter,
        Transaction transaction,
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
                transaction,
                building
            )
        )
            .expandX()
            .left();
    }
}
