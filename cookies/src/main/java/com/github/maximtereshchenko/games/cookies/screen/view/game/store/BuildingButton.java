package com.github.maximtereshchenko.games.cookies.screen.view.game.store;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import com.github.maximtereshchenko.games.cookies.domain.BakeryService;
import com.github.maximtereshchenko.games.cookies.domain.Building;
import com.github.maximtereshchenko.games.cookies.domain.TransactionMode;
import com.github.maximtereshchenko.games.cookies.screen.Assets;
import com.github.maximtereshchenko.games.cookies.screen.view.game.BigDecimalFormatter;
import com.github.maximtereshchenko.games.cookies.screen.view.game.PopUpFrame;

final class BuildingButton extends Button {

    private final Style style;
    private final Transaction transaction;
    private final BakeryService bakeryService;
    private final Building building;

    BuildingButton(
        Actor parent,
        AssetManager assetManager,
        Assets assets,
        BigDecimalFormatter bigDecimalFormatter,
        Transaction transaction,
        BakeryService bakeryService,
        Building building,
        int index
    ) {
        this.style = assetManager.get(assets.game().skin()).get(
            String.valueOf(index % 4),
            Style.class
        );
        this.transaction = transaction;
        this.bakeryService = bakeryService;
        this.building = building;
        add(
            new BuildingIcon(
                assetManager,
                assets,
                "%s-button".formatted(building.name()),
                bakeryService,
                building,
                0.5f
            )
        );
        add(
            new TransactionDetailsWidget(
                assetManager,
                assets,
                bigDecimalFormatter,
                transaction,
                bakeryService,
                building
            )
        ).growX();
        add(
            new BuildingCountLabel(
                assetManager,
                assets,
                bakeryService,
                building
            )
        )
            .padRight(4);
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    transaction.complete(building);
                }
            }
        );
        addListener(
            new BuildingTooltipWidget(
                parent,
                new PopUpFrame(
                    assetManager,
                    assets,
                    new BuildingTooltipPanel(
                        assetManager,
                        assets,
                        bigDecimalFormatter,
                        transaction,
                        bakeryService,
                        building
                    )
                )
            )
        );
        addAction(
            Actions.sequence(
                Actions.fadeOut(0),
                Actions.fadeIn(0.5f)
            )
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setStyle(buttonStyle());
        var isDisabled = shouldDisable();
        if (isDisabled() == isDisabled) {
            return;
        }
        setDisabled(isDisabled);
        addAction(Actions.color(color(isDisabled), 0.5f));
    }

    private ButtonStyle buttonStyle() {
        return buttonStyle(
            switch (transaction.mode()) {
                case BUY -> style.buy;
                case SELL -> style.sell;
            }
        );
    }

    private ButtonStyle buttonStyle(
        Style.TransactionModeStyle transactionModeStyle
    ) {
        var buttonStyle = new ButtonStyle();
        buttonStyle.up = transactionModeStyle.up;
        buttonStyle.disabled = transactionModeStyle.up;
        buttonStyle.down = transactionModeStyle.down;
        buttonStyle.over = transactionModeStyle.over;
        return buttonStyle;
    }

    private boolean shouldDisable() {
        return !transaction.canAfford(building) ||
               (transaction.mode() == TransactionMode.SELL &&
                bakeryService.count(building) == 0);
    }

    private Color color(boolean isDisabled) {
        if (isDisabled) {
            return style.disabledColor;
        }
        return style.enabledColor;
    }

    private static final class Style {

        TransactionModeStyle buy;
        TransactionModeStyle sell;
        Color enabledColor;
        Color disabledColor;

        private static final class TransactionModeStyle {

            Drawable up;
            Drawable down;
            Drawable over;
        }
    }
}
