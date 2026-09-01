package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class GeneratorIcon extends Image implements Subscriber<Event> {

    private final Style style;
    private final Generator generator;

    GeneratorIcon(
        Skin skin,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        var iconStyle = skin.get(generator.name(), Style.class);
        super(iconStyle.drawable);
        this.style = iconStyle;
        this.generator = generator;
        setColor(style.disabledColor);
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorUnlocked generatorUnlocked &&
            generatorUnlocked.value().equals(generator)
        ) {
            addAction(Actions.color(style.enabledColor, 0.5f));
        }
    }

    private static final class Style {

        Drawable drawable;
        Color enabledColor;
        Color disabledColor;
    }
}
