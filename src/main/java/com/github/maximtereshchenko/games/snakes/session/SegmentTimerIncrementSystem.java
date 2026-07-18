package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class SegmentTimerIncrementSystem extends OnFoodEatenEventSystem {

    private final Dominion dominion;
    private final int step;

    SegmentTimerIncrementSystem(Dominion dominion, int step) {
        super(dominion);
        this.dominion = dominion;
        this.step = step;
    }

    @Override
    void onFoodEaten() {
        for (var results : dominion.findEntitiesWith(Timer.class, Segment.class)) {
            results.comp1().turnsLeft += step;
        }
    }
}
