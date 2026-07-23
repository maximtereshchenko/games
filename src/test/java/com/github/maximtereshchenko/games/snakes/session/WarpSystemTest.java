package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class WarpSystemTest {

    private final Dominion dominion = Dominion.create();
    private final WarpSystem warpSystem = new WarpSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new Warp(new Position(0, 0), RelativeDirection.LEFT),
            new Position(1, 1)
        );
        dominion.createEntity(
            Head.INSTANCE,
            new Position(1, 1),
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.RIGHT)
        );
        warpSystem.run(0);
        assertThat(dominion.findEntitiesWith(Warp.class, Position.class))
            .singleElement()
            .extracting(Results.With2::comp1, Results.With2::comp2)
            .containsExactly(
                new Warp(new Position(0, 0), RelativeDirection.LEFT),
                new Position(1, 1)
            );
        assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Position.class,
                CurrentForwardDirection.class,
                NextForwardDirection.class
            )
        )
            .singleElement()
            .extracting(
                Results.With4::comp2,
                result -> result.comp3().value,
                result -> result.comp4().value
            )
            .containsExactly(
                new Position(1, 1),
                Direction.RIGHT,
                Direction.RIGHT
            );
    }

    @Test
    void givenNoHeadOnWarp_thenNoChanges() {
        dominion.createEntity(
            new Warp(new Position(0, 0), RelativeDirection.LEFT),
            new Position(1, 1)
        );
        dominion.createEntity(
            Head.INSTANCE,
            new Position(0, 0),
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.RIGHT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        warpSystem.run(0);
        assertThat(dominion.findEntitiesWith(Warp.class, Position.class))
            .singleElement()
            .extracting(Results.With2::comp1, Results.With2::comp2)
            .containsExactly(
                new Warp(new Position(0, 0), RelativeDirection.LEFT),
                new Position(1, 1)
            );
        assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Position.class,
                CurrentForwardDirection.class,
                NextForwardDirection.class
            )
        )
            .singleElement()
            .extracting(
                Results.With4::comp2,
                result -> result.comp3().value,
                result -> result.comp4().value
            )
            .containsExactly(
                new Position(0, 0),
                Direction.RIGHT,
                Direction.RIGHT
            );
    }

    @Test
    void givenHeadOnWarp_thenHeadPositionDirectionChanged() {
        dominion.createEntity(
            new Warp(new Position(0, 0), RelativeDirection.LEFT),
            new Position(1, 1)
        );
        dominion.createEntity(
            Head.INSTANCE,
            new Position(1, 1),
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.RIGHT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        warpSystem.run(0);
        assertThat(dominion.findEntitiesWith(Warp.class, Position.class))
            .singleElement()
            .extracting(Results.With2::comp1, Results.With2::comp2)
            .containsExactly(
                new Warp(new Position(0, 0), RelativeDirection.LEFT),
                new Position(1, 1)
            );
        assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Position.class,
                CurrentForwardDirection.class,
                NextForwardDirection.class
            )
        )
            .singleElement()
            .extracting(
                Results.With4::comp2,
                result -> result.comp3().value,
                result -> result.comp4().value
            )
            .containsExactly(
                new Position(0, 0),
                Direction.UP,
                Direction.UP
            );
    }
}