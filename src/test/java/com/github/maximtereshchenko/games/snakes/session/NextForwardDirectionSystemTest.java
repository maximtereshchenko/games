package com.github.maximtereshchenko.games.snakes.session;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class NextForwardDirectionSystemTest {

    private final Dominion dominion = Dominion.create();
    private final NextForwardDirectionSystem nextDirectionSystem =
        new NextForwardDirectionSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.UP),
            new LegalRelativeDirection(RelativeDirection.RIGHT)
        );
        nextDirectionSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                CurrentForwardDirection.class,
                NextForwardDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.RIGHT, Direction.UP);
    }

    @Test
    void givenNonLegalDirection_thenNextDirectionRevertedToCurrent() {
        dominion.createEntity(
            new CurrentForwardDirection(Direction.UP),
            new NextForwardDirection(Direction.RIGHT),
            new LegalRelativeDirection(RelativeDirection.LEFT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        nextDirectionSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                CurrentForwardDirection.class,
                NextForwardDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.UP, Direction.UP);
    }

    @Test
    void givenLegalDirection_thenNextDirectionUnchanged() {
        dominion.createEntity(
            new CurrentForwardDirection(Direction.UP),
            new NextForwardDirection(Direction.RIGHT),
            new LegalRelativeDirection(RelativeDirection.RIGHT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        nextDirectionSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                CurrentForwardDirection.class,
                NextForwardDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.UP, Direction.RIGHT);
    }
}
