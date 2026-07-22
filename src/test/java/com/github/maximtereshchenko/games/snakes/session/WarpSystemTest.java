package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.configuration.Mode;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class WarpSystemTest {

    private final Dominion dominion = Dominion.create();
    private final Mode mode = mock();
    private final WarpSystem warpSystem = new WarpSystem(dominion, mode);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new WorldDimensions(1, 1));
        dominion.createEntity(Warp.INSTANCE, new Position(0, 0));
        dominion.createEntity(
            Head.INSTANCE,
            new Position(0, 0),
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.RIGHT)
        );
        warpSystem.run(0);
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
                results -> results.comp3().value,
                results -> results.comp4().value
            )
            .containsExactly(
                new Position(0, 0),
                Direction.RIGHT,
                Direction.RIGHT
            );
    }

    @Test
    void givenNoHeadOnWarp_thenNoChanges() {
        dominion.createEntity(new WorldDimensions(1, 1));
        dominion.createEntity(Warp.INSTANCE, new Position(1, 0));
        dominion.createEntity(
            Head.INSTANCE,
            new Position(0, 0),
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.RIGHT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        warpSystem.run(0);
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
                results -> results.comp3().value,
                results -> results.comp4().value
            )
            .containsExactly(
                new Position(0, 0),
                Direction.RIGHT,
                Direction.RIGHT
            );
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
                    OPPOSITE, UP, 1, 3, UP, 1, 1
                    OPPOSITE, UP, 2, 3, UP, 2, 1
                    OPPOSITE, UP, 3, 3, UP, 3, 1
                    OPPOSITE, UP, 4, 3, UP, 4, 1
                    
                    OPPOSITE, RIGHT, 5, 1, RIGHT, 1, 1
                    OPPOSITE, RIGHT, 5, 2, RIGHT, 1, 2
                    
                    OPPOSITE, DOWN, 1, 0, DOWN, 1, 2
                    OPPOSITE, DOWN, 2, 0, DOWN, 2, 2
                    OPPOSITE, DOWN, 3, 0, DOWN, 3, 2
                    OPPOSITE, DOWN, 4, 0, DOWN, 4, 2
                    
                    OPPOSITE, LEFT, 0, 1, LEFT, 4, 1
                    OPPOSITE, LEFT, 0, 2, LEFT, 4, 2
                    
                    RIGHT, UP, 1, 3, LEFT, 4, 2
                    RIGHT, UP, 2, 3, LEFT, 4, 2
                    RIGHT, UP, 3, 3, LEFT, 4, 1
                    RIGHT, UP, 4, 3, LEFT, 4, 1
                    
                    RIGHT, RIGHT, 5, 1, UP, 1, 1
                    RIGHT, RIGHT, 5, 2, UP, 3, 1
                    
                    RIGHT, DOWN, 1, 0, RIGHT, 1, 2
                    RIGHT, DOWN, 2, 0, RIGHT, 1, 2
                    RIGHT, DOWN, 3, 0, RIGHT, 1, 1
                    RIGHT, DOWN, 4, 0, RIGHT, 1, 1
                    
                    RIGHT, LEFT, 0, 1, DOWN, 1, 2
                    RIGHT, LEFT, 0, 2, DOWN, 3, 2
                    
                    LEFT, UP, 1, 3, RIGHT, 1, 1
                    LEFT, UP, 2, 3, RIGHT, 1, 1
                    LEFT, UP, 3, 3, RIGHT, 1, 2
                    LEFT, UP, 4, 3, RIGHT, 1, 2
                    
                    LEFT, RIGHT, 5, 1, DOWN, 4, 2
                    LEFT, RIGHT, 5, 2, DOWN, 2, 2
                    
                    LEFT, DOWN, 1, 0, LEFT, 4, 1
                    LEFT, DOWN, 2, 0, LEFT, 4, 1
                    LEFT, DOWN, 3, 0, LEFT, 4, 2
                    LEFT, DOWN, 4, 0, LEFT, 4, 2
                    
                    LEFT, LEFT, 0, 1, UP, 4, 1
                    LEFT, LEFT, 0, 2, UP, 2, 1
                    """
    )
    void givenHeadOnWarp_thenHeadWarped(
        Edge edge,
        Direction direction,
        int x,
        int y,
        Direction expectedDirection,
        int expectedX,
        int expectedY
    ) {
        when(mode.warpEdge()).thenReturn(edge);
        dominion.createEntity(new WorldDimensions(6, 4));
        dominion.createEntity(Warp.INSTANCE, new Position(x, y));
        dominion.createEntity(
            Head.INSTANCE,
            new Position(x, y),
            new CurrentForwardDirection(direction),
            new NextForwardDirection(direction)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        warpSystem.run(0);
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
                results -> results.comp3().value,
                results -> results.comp4().value
            )
            .containsExactly(
                new Position(expectedX, expectedY),
                expectedDirection,
                expectedDirection
            );
    }
}