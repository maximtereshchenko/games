package com.github.maximtereshchenko.games.cookies.screen.view.store;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.*;

import java.math.BigDecimal;

final class BuildingButton extends Button implements Subscriber<Event> {

    private final Style style;
    private final Building building;
    private final TransactionDetailsWidget transactionDetailsWidget;
    private final BuildingTooltipPanel buildingTooltip;
    private BigDecimal cookieBalance;
    private BigDecimal transactionValue;

    BuildingButton(
        Skin skin,
        I18NBundle bundle,
        Building building,
        BakeryService bakeryService,
        EventBus<Event> eventBus
    ) {
        var buttonStyle = skin.get(Style.class);
        super(buttonStyle);
        this.style = buttonStyle;
        this.building = building;
        this.transactionDetailsWidget = new TransactionDetailsWidget(
            skin,
            bundle,
            building,
            eventBus
        );
        this.buildingTooltip = new BuildingTooltipPanel(
            skin,
            bundle,
            building,
            eventBus
        );
        this.cookieBalance = BigDecimal.ZERO;
        this.transactionValue = BigDecimal.ZERO;
        add(
            new BuildingIcon(
                skin,
                "icon_%s_buildingButton".formatted(building.name()),
                0.5f,
                building,
                eventBus
            )
        );
        add(transactionDetailsWidget).growX();
        add(
            new BuildingCountLabel(
                skin,
                "label_buildingCount_button",
                bundle,
                "buildings.counts.button.zero",
                "buildings.counts.button",
                building,
                eventBus
            )
        )
            .padRight(4);
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    bakeryService.completeTransaction(building);
                }
            }
        );
        addListener(new TooltipWidget(skin, buildingTooltip));
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        switch (event) {
            case CookieBalanceUpdated cookieBalanceUpdated -> {
                cookieBalance = cookieBalanceUpdated.value();
                setState();
            }
            case TransactionValueUpdated transactionValueUpdated
                when transactionValueUpdated.building() == building -> {
                transactionValue = transactionValueUpdated.value();
                setState();
            }
            default -> {
                //empty
            }
        }
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        super.setDisabled(isDisabled);
        addAction(Actions.color(color(isDisabled), 0.5f));
        transactionDetailsWidget.setDisabled(isDisabled);
        buildingTooltip.setDisabled(isDisabled);
    }

    private void setState() {
        setDisabled(cookieBalance.compareTo(transactionValue) < 0);
    }

    private Color color(boolean isDisabled) {
        if (isDisabled) {
            return style.disabledColor;
        }
        return style.enabledColor;
    }

    private static final class Style extends ButtonStyle {

        Color enabledColor;
        Color disabledColor;
    }
}
