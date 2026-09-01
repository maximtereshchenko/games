package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class GeneratorNameLabel extends Label implements Subscriber<Event> {

    private final Style style;
    private final I18NBundle bundle;
    private final Generator generator;

    GeneratorNameLabel(
        Skin skin,
        I18NBundle bundle,
        Generator generator,
        EventBus<Event> eventBus
    ) {
        var labelStyle = skin.get(generator.name(), Style.class);
        super(
            "???",
            new LabelStyle(
                labelStyle.font,
                labelStyle.disabledFontColor
            )
        );
        this.style = labelStyle;
        this.bundle = bundle;
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
            setText(bundle.get("generators.%s.name".formatted(generator)));
        }
    }

    private static final class Style {

        BitmapFont font;
        Color enabledFontColor;
        Color disabledFontColor;
    }
}
