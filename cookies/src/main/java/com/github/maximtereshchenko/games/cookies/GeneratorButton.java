package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

import java.math.BigDecimal;

final class GeneratorButton extends Button implements Subscriber<Event> {

    private final Style style;
    private final Generator generator;
    private final GeneratorDetailsPanel generatorDetailsPanel;
    private BigDecimal cookies;
    private BigDecimal priceCookies;

    GeneratorButton(
        Skin skin,
        I18NBundle bundle,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        var buttonStyle = skin.get(Style.class);
        super(buttonStyle);
        this.style = buttonStyle;
        this.generator = generator;
        this.generatorDetailsPanel = new GeneratorDetailsPanel(
            skin,
            bundle,
            generator,
            eventBus
        );
        this.cookies = BigDecimal.ZERO;
        this.priceCookies = BigDecimal.ZERO;
        add(new GeneratorIcon(skin, generator, eventBus));
        add(generatorDetailsPanel)
            .growX();
        add(new Label("", skin, "label_generatorAmount"))
            .padRight(4);
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
                when generatorPriceUpdated.generator() == generator -> {
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
        setColor(color(isDisabled));
        generatorDetailsPanel.setDisabled(isDisabled);
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
