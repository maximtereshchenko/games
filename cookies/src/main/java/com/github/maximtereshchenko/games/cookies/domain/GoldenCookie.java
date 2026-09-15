package com.github.maximtereshchenko.games.cookies.domain;

import java.util.Random;

final class GoldenCookie {

    private final Configuration.GoldenCookieConfiguration configuration;
    private final Random random;
    private float durationSeconds;
    private float spawnSeconds;

    GoldenCookie(
        Configuration.GoldenCookieConfiguration configuration,
        Random random
    ) {
        this.configuration = configuration;
        this.random = random;
        this.durationSeconds = 0;
        this.spawnSeconds = 0;
    }

    void update(float deltaTimeSeconds) {
        durationSeconds = Math.max(0, durationSeconds - deltaTimeSeconds);
        if (durationSeconds != 0) {
            return;
        }
        spawnSeconds += deltaTimeSeconds;
        var activeSpawnDurationSeconds = spawnSeconds -
                                         configuration.baseCooldownDurationSeconds();
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
            durationSeconds = configuration.baseDurationSeconds();
            spawnSeconds = 0;
        }
    }

    void reset() {
        durationSeconds = 0;
    }

    Interval interval() {
        return new Interval(
            durationSeconds,
            configuration.baseDurationSeconds()
        );
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
            configuration.baseSpawnDurationSeconds(),
            0.0,
            1.0
        );
        return 1.0 - Math.pow(progress, 5);
    }
}
