package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class GeneratorButton extends Button implements Subscriber<Event> {

    private final Style style;
    private final Generator generator;

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
        setDisabled(true);
        add(new GeneratorIcon(skin, generator, eventBus));
        add(new GeneratorDetailsPanel(skin, bundle, generator, eventBus))
            .growX();
        add(new Label("", skin, "label_generatorAmount"))
            .padRight(4);
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorUnlocked generatorUnlocked &&
            generatorUnlocked.value().equals(generator)
        ) {
            setDisabled(false);
        }
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        super.setDisabled(isDisabled);
        setColor(color(isDisabled));
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
