package com.github.maximtereshchenko.games.cookies;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;

final class CookieAmountLabel extends CookiesLabel implements Subscriber<Event> {

    CookieAmountLabel(Skin skin, EventBus<Event> eventBus) {
        super("", skin);
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof CookieAmountUpdated cookieAmountUpdated) {
            setText(cookieAmountUpdated.value().toPlainString());
        }
    }
}
