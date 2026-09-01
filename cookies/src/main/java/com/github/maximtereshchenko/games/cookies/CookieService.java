package com.github.maximtereshchenko.games.cookies;

import com.github.maximtereshchenko.games.common.event.EventBus;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

final class CookieService {

    private static final Map<Generator, BigDecimal> BASE_COST = Map.of(
        Generator.CURSOR, BigDecimal.valueOf(15)
    );

    private final EventBus<Event> eventBus;
    private final Map<Generator, Integer> generators;
    private BigDecimal cookieAmount;

    CookieService(EventBus<Event> eventBus) {
        this.eventBus = eventBus;
        this.generators = new EnumMap<>(Generator.class);
        this.cookieAmount = BigDecimal.ZERO;
    }

    void onStart() {
        eventBus.publish(new CookieAmountUpdated(cookieAmount));
    }

    void onClick() {
        var clickPower = BigDecimal.ONE;
        cookieAmount = cookieAmount.add(clickPower);
        eventBus.publish(new CookiesClicked(clickPower));
        eventBus.publish(new CookieAmountUpdated(cookieAmount));
        for (var entry : BASE_COST.entrySet()) {
            if (
                !generators.containsKey(entry.getKey()) &&
                cookieAmount.compareTo(entry.getValue()) >= 0
            ) {
                generators.put(entry.getKey(), 0);
                eventBus.publish(new GeneratorUnlocked(entry.getKey()));
            }
        }
    }
}
