package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.configuration.Configuration;
import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class EntityFactory {

    private final Configuration configuration;

    public EntityFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    void createWorld(Dominion dominion, WorldDimensions worldDimensions) {
        dominion.createEntity(new Session());
        dominion.createEntity(worldDimensions);
        dominion.createEntity(new TurnTimer());
        dominion.createEntity(new InitialSegmentTimer(configuration.snakeLength()));
        dominion.createEntity(new SessionStatisticsAccumulator());
        dominion.createEntity(new FoodEatenCounter(0), Colored.FOOD_EATEN_COUNTER);
        createBackgrounds(dominion, worldDimensions);
        createWarps(dominion, worldDimensions);
    }

    void createHead(Dominion dominion, Mode mode) {
        dominion.createEntity(
            Head.INSTANCE,
            new CurrentForwardDirection(configuration.snakeHeadForwardDirection()),
            new NextForwardDirection(configuration.snakeHeadForwardDirection()),
            new SidewaysDirection(mode.headMovementSidewaysCycle()),
            new Position(configuration.snakeHeadPosition()),
            new Timer(mode.headMovementSidewaysInterval()),
            Colored.HEAD
        );
    }

    void createSegment(Dominion dominion, Position position, int turnsRemaining) {
        dominion.createEntity(
            Segment.INSTANCE,
            position,
            new Timer(turnsRemaining),
            Colored.SEGMENT
        );
    }

    void createFoodEatenEvent(Dominion dominion) {
        createEvent(dominion, FoodEaten.INSTANCE);

    }

    void createFood(Dominion dominion, Position position) {
        dominion.createEntity(Food.INSTANCE, position, Colored.FOOD);
    }

    void createTurnStartedEvent(Dominion dominion) {
        createEvent(dominion, TurnStarted.INSTANCE);
    }

    private void createBackgrounds(Dominion dominion, WorldDimensions worldDimensions) {
        for (var x = 0; x < worldDimensions.width(); x++) {
            for (var y = 0; y < worldDimensions.height(); y++) {
                dominion.createEntity(
                    Background.INSTANCE,
                    new Position(x, y),
                    Colored.BACKGROUND
                );
            }
        }
    }

    private void createWarps(Dominion dominion, WorldDimensions worldDimensions) {
        Stream.concat(
                positions(
                    worldDimensions.width(),
                    worldDimensions.height(),
                    Position::new
                ),
                positions(
                    worldDimensions.height(),
                    worldDimensions.width(),
                    (y, x) -> new Position(x, y)
                )
            )
            .distinct()
            .forEach(position -> dominion.createEntity(Warp.INSTANCE, position, Colored.WARP));
    }

    private Stream<Position> positions(
        int firstBorder,
        int secondBorder,
        BiFunction<Integer, Integer, Position> function
    ) {
        return IntStream.range(0, firstBorder)
            .mapToObj(
                first ->
                    IntStream.of(0, secondBorder - 1)
                        .mapToObj(second -> function.apply(first, second))
            )
            .flatMap(Function.identity());
    }

    private void createEvent(Dominion dominion, Object tag) {
        dominion.createEntity(tag, Event.INSTANCE);
    }
}
