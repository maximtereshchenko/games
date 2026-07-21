package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.Configuration;
import dev.dominion.ecs.api.Dominion;

final class InitialSegmentTimerSystem extends OnFoodEatenEventSystem {

    private final Dominion dominion;
    private final Configuration configuration;

    InitialSegmentTimerSystem(Dominion dominion, Configuration configuration) {
        super(dominion);
        this.dominion = dominion;
        this.configuration = configuration;
    }

    @Override
    void onFoodEaten() {
        for (var initialSegmentTimer : dominion.findCompositionsWith(InitialSegmentTimer.class)) {
            initialSegmentTimer.value += configuration.snakeFoodGrowth();
        }
    }
}
