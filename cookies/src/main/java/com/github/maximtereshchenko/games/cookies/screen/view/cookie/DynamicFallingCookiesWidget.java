package com.github.maximtereshchenko.games.cookies.screen.view.cookie;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.CookiesClicked;
import com.github.maximtereshchenko.games.cookies.domain.Event;

import java.util.Random;

final class DynamicFallingCookiesWidget extends WidgetGroup implements Subscriber<Event> {

    private final Skin skin;
    private final Random random;

    DynamicFallingCookiesWidget(
        Skin skin,
        Random random,
        EventBus<Event> eventBus
    ) {
        this.skin = skin;
        this.random = random;
        eventBus.subscribe(this);
        setLayoutEnabled(false);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof CookiesClicked) {
            addActor(new CookieIcon(skin, random, getWidth(), getHeight()));
        }
    }
}
