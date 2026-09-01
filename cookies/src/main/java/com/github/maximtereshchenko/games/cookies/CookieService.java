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
    private BigDecimal cookies;

    CookieService(EventBus<Event> eventBus) {
        this.eventBus = eventBus;
        this.generators = new EnumMap<>(Generator.class);
        this.cookies = BigDecimal.ZERO;
    }

    void onStart() {
        eventBus.publish(new CookieAmountUpdated(cookies));
        for (var entry : BASE_COST.entrySet()) {
            eventBus.publish(
                new GeneratorPriceUpdated(
                    entry.getKey(),
                    entry.getValue()
                )
            );
        }
    }

    void onClick() {
        var clickPower = BigDecimal.ONE;
        cookies = cookies.add(clickPower);
        eventBus.publish(new CookiesClicked(clickPower));
        eventBus.publish(new CookieAmountUpdated(cookies));
        for (var entry : BASE_COST.entrySet()) {
            if (
                !generators.containsKey(entry.getKey()) &&
                cookies.compareTo(entry.getValue()) >= 0
            ) {
                generators.put(entry.getKey(), 0);
                eventBus.publish(
                    new GeneratorUnlocked(entry.getKey())
                );
            }
        }
    }

    void buyGenerator(Generator generator) {
        cookies = cookies.subtract(BASE_COST.get(generator));
        var amount = generators.get(generator) + 1;
        generators.put(generator, amount);
        eventBus.publish(new CookieAmountUpdated(cookies));
        eventBus.publish(
            new GeneratorBought(generator, amount)
        );
    }
}
