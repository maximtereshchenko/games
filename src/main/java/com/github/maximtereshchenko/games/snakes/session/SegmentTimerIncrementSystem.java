package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.configuration.Configuration;
import dev.dominion.ecs.api.Dominion;

final class SegmentTimerIncrementSystem extends OnFoodEatenEventSystem {

    private final Dominion dominion;
    private final Configuration configuration;

    SegmentTimerIncrementSystem(Dominion dominion, Configuration configuration) {
        super(dominion);
        this.dominion = dominion;
        this.configuration = configuration;
    }

    @Override
    void onFoodEaten() {
        for (var results : dominion.findEntitiesWith(Timer.class, Segment.class)) {
            results.comp1().turnsRemaining += configuration.snakeFoodGrowth();
        }
    }
}
