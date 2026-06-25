package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadMovementSystemTest {

    private final Dominion dominion = Dominion.create();
    private final EntityFactory entityFactory = new EntityFactory(dominion);
    private final HeadMovementSystem headMovementSystem = new HeadMovementSystem(
        dominion,
        new FitViewport(3, 3)
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        entityFactory.createHead(HeadDirection.UP, new Point());
        headMovementSystem.run();
        assertThat(dominion.findEntitiesWith(HeadDirection.class, Point.class))
            .extracting(Results.With2::comp2)
            .containsExactly(new Point());
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
        HeadDirection headDirection,
        int initialX,
        int initialY,
        int expectedX,
        int expectedY
    ) {
        entityFactory.createHead(headDirection, new Point(initialX, initialY));
        entityFactory.createTurnStartedEvent();
        headMovementSystem.run();
        assertThat(dominion.findEntitiesWith(HeadDirection.class, Point.class))
            .extracting(Results.With2::comp2)
            .containsExactly(new Point(expectedX, expectedY));
    }
}