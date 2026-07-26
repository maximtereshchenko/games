package com.github.maximtereshchenko.snakes.session;

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
        for (var warpResult : dominion.findEntitiesWith(Warp.class, HeadCollisionTarget.class)) {
            for (var headResult : dominion.findEntitiesWith(Head.class, Position.class, CurrentForwardDirection.class, NextForwardDirection.class)) {
                var warp = warpResult.comp1();
                setPosition(headResult.entity(), warp.position());
                changeDirection(
                    headResult.comp3(),
                    headResult.comp4(),
                    warp.relativeDirection()
                );
            }
        }
    }

    private void setPosition(Entity entity, Position position) {
        entity.removeType(Position.class);
        entity.add(new Position(position));
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
