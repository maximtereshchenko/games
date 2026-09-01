package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class SmallCookieIcon extends Image implements Subscriber<Event> {

    private final Style style;
    private final Generator generator;

    SmallCookieIcon(Skin skin, Generator generator, EventBus<Event> eventBus) {
        var imageStyle = skin.get(Style.class);
        super(imageStyle.drawable);
        this.style = imageStyle;
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
