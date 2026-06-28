package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.Point;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadMovementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final HeadMovementSystem headMovementSystem = new HeadMovementSystem(
        dominion,
        new FitViewport(3, 3)
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new Head(Head.Direction.RIGHT),
            Tail.INSTANCE,
            new Point(0, 0),
            Colors.HEAD
        );
        var before = dominion.findAllEntities().stream().toList();
        headMovementSystem.run();
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    UP, 1, 1, 1, 2
                    DOWN, 1, 1, 1, 0
                    LEFT, 1, 1, 0, 1
                    RIGHT, 1, 1, 2, 1
                    UP, 2, 2, 2, 0
                    DOWN, 0, 0, 0, 2
                    LEFT, 0, 0, 2, 0
                    RIGHT, 2, 2, 0, 2
                    """
    )
    void givenTurnStartedEvent_thenHeadMoved(
        Head.Direction direction,
        int initialX,
        int initialY,
        int expectedX,
        int expectedY
    ) {
        dominion.createEntity(
            new Head(direction),
            Tail.INSTANCE,
            new Point(initialX, initialY),
            Colors.HEAD
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        headMovementSystem.run();
        var headResult = assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Point.class,
                Previous.class,
                Color.class
            )
        )
            .singleElement()
            .actual();
        assertThat(headResult.comp1().direction).isEqualTo(direction);
        assertThat(headResult.comp2()).isEqualTo(new Point(expectedX, expectedY));
        assertThat(headResult.comp4()).isEqualTo(Colors.HEAD);
        var tailResult = assertThat(
            dominion.findEntitiesWith(
                Tail.class,
                Point.class,
                Next.class,
                Color.class
            )
        )
            .singleElement()
            .actual();
        assertThat(tailResult.comp2()).isEqualTo(new Point(initialX, initialY));
        assertThat(tailResult.comp4()).isEqualTo(Colors.SEGMENT);
        assertThat(headResult.comp3().entity).isEqualTo(tailResult.entity());
        assertThat(tailResult.comp3().entity).isEqualTo(headResult.entity());
    }

    @Test
    void givenTail_thenSegmentCreated() {
        var head = dominion.createEntity(
            new Head(Head.Direction.RIGHT),
            new Point(1, 0),
            Colors.HEAD
        );
        head.add(
            new Previous(
                dominion.createEntity(
                    Tail.INSTANCE,
                    new Next(head),
                    new Point(0, 0),
                    Colors.SEGMENT
                )
            )
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        headMovementSystem.run();
        var headResult = assertThat(
            dominion.findEntitiesWith(
                Head.class,
                Point.class,
                Previous.class,
                Color.class
            )
        )
            .singleElement()
            .actual();
        assertThat(headResult.comp1().direction).isEqualTo(Head.Direction.RIGHT);
        assertThat(headResult.comp2()).isEqualTo(new Point(2, 0));
        assertThat(headResult.comp4()).isEqualTo(Colors.HEAD);
        var tailResult = assertThat(
            dominion.findEntitiesWith(
                Tail.class,
                Point.class,
                Next.class,
                Color.class
            )
        )
            .singleElement()
            .actual();
        assertThat(tailResult.comp2()).isEqualTo(new Point(0, 0));
        assertThat(tailResult.comp4()).isEqualTo(Colors.SEGMENT);
        var segmentResult = assertThat(
            dominion.findEntitiesWith(
                Point.class,
                Previous.class,
                Next.class,
                Color.class
            )
        )
            .filteredOn(result -> !result.entity().has(Head.class))
            .filteredOn(result -> !result.entity().has(Tail.class))
            .singleElement()
            .actual();
        assertThat(segmentResult.comp1()).isEqualTo(new Point(1, 0));
        assertThat(segmentResult.comp4()).isEqualTo(Colors.SEGMENT);
        assertThat(headResult.comp3().entity).isEqualTo(segmentResult.entity());
        assertThat(tailResult.comp3().entity).isEqualTo(segmentResult.entity());
        assertThat(segmentResult.comp2().entity).isEqualTo(tailResult.entity());
        assertThat(segmentResult.comp3().entity).isEqualTo(headResult.entity());
    }
}