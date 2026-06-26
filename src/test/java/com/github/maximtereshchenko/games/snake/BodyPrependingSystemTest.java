package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.assertj.core.api.Assertions.assertThat;

final class BodyPrependingSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EntityFactory entityFactory = new EntityFactory(dominion);
    private final BodyPrependingSystem bodyPrependingSystem = new BodyPrependingSystem(
        dominion,
        entityFactory
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        entityFactory.createHead(HeadDirection.UP, new Point(0, 0));
        bodyPrependingSystem.run();
        assertThat(dominion.findCompositionsWith(Segment.class, Point.class, Color.class))
            .isEmpty();
    }

    @Test
    void givenTurnStartedEvent_thenSegment() {
        entityFactory.createHead(HeadDirection.UP, new Point(0, 0));
        entityFactory.createTurnStartedEvent();
        bodyPrependingSystem.run();
        assertThat(dominion.findCompositionsWith(Segment.class, Point.class, Color.class))
            .singleElement()
            .satisfies(
                result -> assertThat(result.comp2()).isEqualTo(new Point(0, 0)),
                result -> assertThat(result.comp3()).isEqualTo(Color.GREEN)
            );
    }

    @Test
    void givenHeadMoved_thenSegmentUnchanged() {
        var headPoint = new Point(0, 0);
        entityFactory.createHead(HeadDirection.UP, headPoint);
        entityFactory.createTurnStartedEvent();
        bodyPrependingSystem.run();
        headPoint.x = 1;
        assertThat(dominion.findCompositionsWith(Segment.class, Point.class, Color.class))
            .singleElement()
            .satisfies(
                result -> assertThat(result.comp2()).isEqualTo(new Point(0, 0)),
                result -> assertThat(result.comp3()).isEqualTo(Color.GREEN)
            );
    }
}