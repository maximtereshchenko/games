package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;

import java.awt.Point;

final class BodyPrependingSystem extends TurnBasedSystem {

    private final Dominion dominion;
    private final EntityFactory entityFactory;

    BodyPrependingSystem(Dominion dominion, EntityFactory entityFactory) {
        super(dominion);
        this.dominion = dominion;
        this.entityFactory = entityFactory;
    }

    @Override
    void onTurnStarted() {
        for (var headResult : dominion.findCompositionsWith(HeadDirection.class, Point.class)) {
            entityFactory.createSegment(new Point(headResult.comp2()));
        }
    }
}
