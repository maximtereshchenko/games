package com.github.maximtereshchenko.games.cookies.domain;

import com.github.maximtereshchenko.games.common.event.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

public final class CookieService {

    private static final Map<Generator, BigDecimal> BASE_PRICES = Map.of(
        Generator.CURSOR, BigDecimal.valueOf(15)
    );
    private static final Map<Generator, BigDecimal> BASE_GENERATION = Map.of(
        Generator.CURSOR, BigDecimal.valueOf(0.1)
    );

    private final EventBus<Event> eventBus;
    private final Map<Generator, Integer> generators;
    private BigDecimal cookies;

    public CookieService(EventBus<Event> eventBus) {
        this.eventBus = eventBus;
        this.generators = new EnumMap<>(Generator.class);
        this.cookies = BigDecimal.ZERO;
    }

    public void onStart() {
        eventBus.publish(new CookieAmountUpdated(cookies));
        eventBus.publish(
            new CookiesPerSecondUpdated(cookiesPerSecond())
        );
        for (var entry : BASE_PRICES.entrySet()) {
            eventBus.publish(
                new GeneratorPriceUpdated(
                    entry.getKey(),
                    entry.getValue()
                )
            );
        }
    }

    public void update(float deltaTimeSeconds) {
        cookies = cookies.add(
            cookiesPerSecond()
                .multiply(
                    BigDecimal.valueOf(deltaTimeSeconds)
                )
        );
        eventBus.publish(new CookieAmountUpdated(cookies));
    }

    public void onClick() {
        var clickPower = BigDecimal.ONE;
        cookies = cookies.add(clickPower);
        eventBus.publish(new CookiesClicked(clickPower));
        eventBus.publish(new CookieAmountUpdated(cookies));
        for (var entry : BASE_PRICES.entrySet()) {
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

    public void buyGenerator(Generator generator) {
        cookies = cookies.subtract(price(generator));
        var amount = generators.get(generator) + 1;
        generators.put(generator, amount);
        eventBus.publish(new CookieAmountUpdated(cookies));
        eventBus.publish(
            new GeneratorBought(generator, amount)
        );
        eventBus.publish(
            new GeneratorPriceUpdated(generator, price(generator))
        );
        eventBus.publish(
            new CookiesPerSecondUpdated(cookiesPerSecond())
        );
    }

    private BigDecimal price(Generator generator) {
        return BASE_PRICES.get(generator)
            .multiply(
                BigDecimal.valueOf(1.15)
                    .pow(generators.get(generator))
            )
            .setScale(0, RoundingMode.CEILING);
    }

    private BigDecimal cookiesPerSecond() {
        var cookiesPerSecond = BigDecimal.ZERO;
        for (var entry : generators.entrySet()) {
            cookiesPerSecond = cookiesPerSecond.add(
                BASE_GENERATION.get(entry.getKey())
                    .multiply(BigDecimal.valueOf(entry.getValue()))
            );
        }
        return cookiesPerSecond;
    }
}
