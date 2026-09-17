package com.github.maximtereshchenko.games.cookies.domain;

import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;

final class GoldenCookie {

    private final Configuration.GoldenCookieConfiguration configuration;
    private final PlayerProgress playerProgress;
    private final Random random;
    private float remainingDurationSeconds;
    private float durationSeconds;
    private float spawnSeconds;

    GoldenCookie(
        Configuration.GoldenCookieConfiguration configuration,
        PlayerProgress playerProgress,
        Random random
    ) {
        this.configuration = configuration;
        this.playerProgress = playerProgress;
        this.random = random;
        this.remainingDurationSeconds = 0;
        this.durationSeconds = 0;
        this.spawnSeconds = 0;
    }

    void update(float deltaTimeSeconds) {
        remainingDurationSeconds = Math.max(0, remainingDurationSeconds - deltaTimeSeconds);
        if (remainingDurationSeconds != 0) {
            return;
        }
        spawnSeconds += deltaTimeSeconds;
        var activeSpawnDurationSeconds = spawnSeconds - reduced(
            configuration.baseCooldownDurationSeconds()
        );
        if (activeSpawnDurationSeconds < 0) {
            return;
        }
        var previousFailureChance = goldenCookieSpawnFailureProbability(
            activeSpawnDurationSeconds - deltaTimeSeconds
        );
        var currentFailureChance = goldenCookieSpawnFailureProbability(
            activeSpawnDurationSeconds
        );
        if (shouldSpawn(previousFailureChance, currentFailureChance)) {
            durationSeconds = calculated(
                configuration.baseDurationSeconds(),
                (calculated, value) -> calculated * value
            );
            remainingDurationSeconds = durationSeconds;
            spawnSeconds = 0;
        }
    }

    void reset() {
        remainingDurationSeconds = 0;
    }

    Interval interval() {
        return new Interval(remainingDurationSeconds, durationSeconds);
    }

    private float calculated(float base, BinaryOperator<Float> operator) {
        var calculated = base;
        for (var upgrade : List.of(Upgrade.GOLDEN_COOKIE_TIER_0, Upgrade.GOLDEN_COOKIE_TIER_1)) {
            if (playerProgress.activeUpgrades.contains(upgrade)) {
                calculated = operator.apply(calculated, 2f);
            }
        }
        return calculated;
    }

    private float reduced(float base) {
        return calculated(base, (calculated, value) -> calculated / value);
    }

    private boolean shouldSpawn(
        double previousFailureChance,
        double currentFailureChance
    ) {
        return previousFailureChance == 0 ||
               random.nextDouble() < 1.0 - (currentFailureChance / previousFailureChance);
    }

    private double goldenCookieSpawnFailureProbability(
        double activeSpawnDurationSeconds
    ) {
        var progress = Math.clamp(
            activeSpawnDurationSeconds /
            reduced(
                configuration.baseSpawnDurationSeconds()
            ),
            0.0,
            1.0
        );
        return 1.0 - Math.pow(progress, 5);
    }
}
