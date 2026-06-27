package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.assertj.core.api.Assertions.assertThat;

final class TailRemovalSystemTest {

    private final Dominion dominion = Dominion.create();
    private final TailRemovalSystem tailRemovalSystem = new TailRemovalSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(Tail.INSTANCE, new Point(0, 0));
        tailRemovalSystem.run();
        assertThat(dominion.findEntitiesWith(Tail.class, Point.class))
            .extracting(Results.With2::comp2)
            .containsExactly(new Point(0, 0));
    }

    @Test
    void givenTurnStartedEvent_thenTailRemoved() {
        var tail = dominion.createEntity(Tail.INSTANCE, new Point(0, 0));
        tail.add(
            new Next(
                dominion.createEntity(
                    new Head(Head.Direction.RIGHT),
                    new Point(1, 0),
                    new Previous(tail)
                )
            )
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        tailRemovalSystem.run();
        assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Tail.class,
                Point.class
            )
        )
            .singleElement()
            .satisfies(
                result -> assertThat(result.entity().has(Previous.class)).isFalse(),
                result -> assertThat(result.comp1().direction).isEqualTo(Head.Direction.RIGHT),
                result -> assertThat(result.comp3()).isEqualTo(new Point(1, 0))
            );
    }
}