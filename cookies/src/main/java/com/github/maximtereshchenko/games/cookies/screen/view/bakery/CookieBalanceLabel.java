package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.CookieBalanceUpdated;
import com.github.maximtereshchenko.games.cookies.domain.Event;

import java.math.RoundingMode;

final class CookieBalanceLabel extends BaseCookiesLabel implements Subscriber<Event> {

    CookieBalanceLabel(Skin skin, EventBus<Event> eventBus) {
        super("", skin);
        eventBus.subscribe(this);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof CookieBalanceUpdated cookieBalanceUpdated) {
            setText(
                cookieBalanceUpdated.value()
                    .setScale(0, RoundingMode.FLOOR)
                    .toString()
            );
        }
    }
}
