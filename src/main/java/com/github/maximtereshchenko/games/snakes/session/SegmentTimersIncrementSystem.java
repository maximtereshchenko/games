package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;

final class SegmentTimersIncrementSystem extends OnFoodEatenEventSystem {

    private final Dominion dominion;

    SegmentTimersIncrementSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onFoodEaten() {
        for (var initialSegmentTimer : dominion.findCompositionsWith(SegmentTimerDefinition.class)) {
            initialSegmentTimer.duration += initialSegmentTimer.incrementStep;
            for (var results : dominion.findEntitiesWith(Timer.class, Segment.class)) {
                results.comp1().turnsRemaining += initialSegmentTimer.incrementStep;
            }
        }
    }
}
