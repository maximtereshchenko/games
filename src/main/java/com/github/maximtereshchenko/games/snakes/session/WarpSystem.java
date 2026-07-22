package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Entity;

final class WarpSystem extends TurnBasedSystem {

    private final Dominion dominion;

    WarpSystem(Dominion dominion) {
        super(dominion);
        this.dominion = dominion;
    }

    @Override
    void onTurnStarted() {
        for (var warpResults : dominion.findEntitiesWith(Warp.class, Position.class)) {
            for (var headResults : dominion.findEntitiesWith(Head.class, Position.class, CurrentForwardDirection.class, NextForwardDirection.class)) {
                if (warpResults.comp2().equals(headResults.comp2())) {
                    var warp = warpResults.comp1();
                    setPosition(headResults.entity(), warp.position());
                    changeDirection(
                        headResults.comp3(),
                        headResults.comp4(),
                        warp.relativeDirection()
                    );
                }
            }
        }
    }

    private void setPosition(Entity entity, Position position) {
        entity.removeType(Position.class);
        entity.add(position);
    }

    private void changeDirection(
        CurrentForwardDirection currentForwardDirection,
        NextForwardDirection nextForwardDirection,
        RelativeDirection relativeDirection
    ) {
        currentForwardDirection.value = currentForwardDirection.value
            .relative(relativeDirection);
        nextForwardDirection.value = currentForwardDirection.value;
    }
}
