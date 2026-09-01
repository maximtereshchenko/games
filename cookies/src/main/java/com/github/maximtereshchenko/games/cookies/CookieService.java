package com.github.maximtereshchenko.games.cookies;

import com.github.maximtereshchenko.games.common.event.EventBus;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

final class CookieService {

    private final EventBus<Event> eventBus;
    private final Map<Generator, Integer> generators;
    private BigDecimal cookieAmount;
    private final BigDecimal cookiesPerSecond;

    CookieService(EventBus<Event> eventBus) {
        this.eventBus = eventBus;
        this.generators = new EnumMap<>(Generator.class);
        this.cookieAmount = BigDecimal.ZERO;
        this.cookiesPerSecond = BigDecimal.ZERO;
        for (var generator : Generator.values()) {
            generators.put(generator, 0);
        }
    }

    void onStart() {
        eventBus.publish(new CookieAmountUpdated(cookieAmount));
    }

    void onClick() {
        var clickPower = BigDecimal.ONE;
        cookieAmount = cookieAmount.add(clickPower);
        eventBus.publish(new CookiesClicked(clickPower));
        eventBus.publish(new CookieAmountUpdated(cookieAmount));
    }
}
