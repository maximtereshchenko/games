package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;

final class TransactionDetailsWidget extends Table {

    TransactionDetailsWidget(
        Skin skin,
        I18NBundle bundle,
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
        add(new Image(skin.get(Style.class).icon))
            .width(Value.prefWidth)
            .padTop(2);
        add(
            new TransactionValueLabel(
                skin,
                bakeryService,
                building
            )
        )
            .expandX()
            .left();
    }

    private static final class Style {

        Drawable icon;
    }
}
