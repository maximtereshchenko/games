package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.Event;

final class TransactionDetailsWidget extends Table {

    private final TransactionValueLabel transactionValueLabel;

    TransactionDetailsWidget(
        Skin skin,
        I18NBundle bundle,
        Building building,
        EventBus<Event> eventBus
    ) {
        this.transactionValueLabel = new TransactionValueLabel(
            skin,
            building,
            eventBus
        );
        add(
            new BuildingNameLabel(
                skin,
                bundle,
                building,
                eventBus
            )
        )
            .colspan(2)
            .left()
            .padBottom(4)
            .row();
        add(new Image(skin.get(Style.class).icon))
            .width(Value.prefWidth)
            .padTop(2);
        add(transactionValueLabel).expandX().left();
    }

    void setDisabled(boolean isDisabled) {
        transactionValueLabel.setDisabled(isDisabled);
    }

    private static final class Style {

        Drawable icon;
    }
}
