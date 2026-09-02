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
    private BigDecimal cookies;
    private BigDecimal priceCookies;

    BuildingButton(
        Skin skin,
        I18NBundle bundle,
        Building building,
        CookieService cookieService,
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
        this.cookies = BigDecimal.ZERO;
        this.priceCookies = BigDecimal.ZERO;
        add(new BuildingIcon(skin, building, eventBus));
        add(transactionDetailsWidget).growX();
        add(new BuildingCountLabel(skin, building, eventBus))
            .padRight(4);
        addListener(
            new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    cookieService.buyGenerator(building);
                }
            }
        );
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        switch (event) {
            case CookieAmountUpdated cookieAmountUpdated -> {
                cookies = cookieAmountUpdated.value();
                setState();
            }
            case GeneratorPriceUpdated generatorPriceUpdated
                when generatorPriceUpdated.generator() == building -> {
                priceCookies = generatorPriceUpdated.price();
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
    }

    private void setState() {
        setDisabled(cookies.compareTo(priceCookies) < 0);
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
