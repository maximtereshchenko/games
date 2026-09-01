package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class GeneratorCookiesLabel extends Label implements Subscriber<Event> {

    private final Style style;
    private final Generator generator;

    GeneratorCookiesLabel(
        Skin skin,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        var labelStyle = skin.get(Style.class);
        super(
            "15",
            new LabelStyle(
                labelStyle.font,
                labelStyle.disabledFontColor
            )
        );
        this.style = labelStyle;
        this.generator = generator;
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorUnlocked generatorUnlocked &&
            generatorUnlocked.value().equals(generator)
        ) {
            setStyle(new LabelStyle(style.font, style.enabledFontColor));
        }
    }

    private static final class Style {

        BitmapFont font;
        Color enabledFontColor;
        Color disabledFontColor;
    }
}
