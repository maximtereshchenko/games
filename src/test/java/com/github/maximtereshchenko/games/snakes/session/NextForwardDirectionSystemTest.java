package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.Mode;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class NextForwardDirectionSystemTest {

    private final Dominion dominion = Dominion.create();
    private final Mode mode = mock();
    private final NextForwardDirectionSystem nextDirectionSystem =
        new NextForwardDirectionSystem(dominion, mode);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.UP)
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
            new NextForwardDirection(Direction.RIGHT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        when(mode.legalTurnDirections()).thenReturn(Set.of(RelativeDirection.LEFT));
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
            new NextForwardDirection(Direction.RIGHT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        when(mode.legalTurnDirections()).thenReturn(Set.of(RelativeDirection.RIGHT));
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
