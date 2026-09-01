package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class SmallCookieIcon extends Image implements Subscriber<Event> {

    private final Generator generator;

    SmallCookieIcon(Skin skin, Generator generator, EventBus<Event> eventBus) {
        super(skin, "icon_cookie_small");
        this.generator = generator;
        setColor(skin.getColor("color_white_transparent60"));
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorUnlocked generatorUnlocked &&
            generatorUnlocked.value().equals(generator)
        ) {
            addAction(Actions.color(Color.WHITE, 0.5f));
        }
    }
}
