package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.Configuration;
import dev.dominion.ecs.api.Dominion;

public final class EntityFactory {

    private final Configuration configuration;

    public EntityFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    void createGlobals(Dominion dominion, WorldDimensions worldDimensions) {
        dominion.createEntity(new Session());
        dominion.createEntity(worldDimensions);
        dominion.createEntity(new TurnTimer());
        dominion.createEntity(new InitialSegmentTimer(configuration.snakeLength()));
        dominion.createEntity(new SessionStatisticsAccumulator());
        dominion.createEntity(new FoodEatenCounter(0), Colored.FOOD_EATEN_COUNTER);
    }

    void createHead(Dominion dominion) {
        dominion.createEntity(
            Head.INSTANCE,
            new CurrentForwardDirection(configuration.snakeHeadForwardDirection()),
            new NextForwardDirection(configuration.snakeHeadForwardDirection()),
            configuration.snakeHeadPosition(),
            Colored.HEAD
        );
    }

    void createSegment(Dominion dominion, Position position, int turnsLeft) {
        dominion.createEntity(
            Segment.INSTANCE,
            position,
            new Timer(turnsLeft),
            Colored.SEGMENT
        );
    }

    void createFoodEatenEvent(Dominion dominion) {
        dominion.createEntity(FoodEaten.INSTANCE, Event.INSTANCE);
    }

    void createFood(Dominion dominion, Position position) {
        dominion.createEntity(Food.INSTANCE, position, Colored.FOOD);
    }

    void createTurnStartedEvent(Dominion dominion) {
        dominion.createEntity(TurnStarted.INSTANCE, Event.INSTANCE);
    }
}
