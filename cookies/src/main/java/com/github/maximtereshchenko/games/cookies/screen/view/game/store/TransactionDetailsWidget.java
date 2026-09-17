package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.game.SmallCookieIcon;

final class TransactionDetailsWidget extends Table {

    TransactionDetailsWidget(
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        Transaction transaction,
        BakeryService bakeryService,
        Building building
    ) {
        add(
            new BuildingNameLabel(
                assetManager,
                assets,
                "building-name-button",
                bakeryService,
                building
            )
        )
            .colspan(2)
            .left()
            .padBottom(4)
            .row();
        add(new SmallCookieIcon(assetManager, assets))
            .width(Value.prefWidth)
            .padTop(2);
        add(
            new TransactionValueLabel(
                assetManager,
                assets,
                bigDecimalFormatter,
                transaction,
                building
            )
        )
            .expandX()
            .left();
    }
}
