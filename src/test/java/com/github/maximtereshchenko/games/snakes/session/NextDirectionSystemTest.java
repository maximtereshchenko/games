package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.Mode;
import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class NextDirectionSystemTest {

    private final Dominion dominion = Dominion.create();
    private final Mode mode = mock();
    private final NextDirectionSystem nextDirectionSystem = new NextDirectionSystem(
        dominion,
        mode
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(
            new CurrentDirection(Direction.RIGHT),
            new NextDirection(Direction.UP)
        );
        nextDirectionSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                CurrentDirection.class,
                NextDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.RIGHT, Direction.UP);
    }

    @Test
    void givenNonLegalDirection_thenNextDirectionRevertedToCurrent() {
        dominion.createEntity(
            new CurrentDirection(Direction.UP),
            new NextDirection(Direction.RIGHT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        when(mode.legalTurns()).thenReturn(Set.of(LegalTurn.LEFT));
        nextDirectionSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                CurrentDirection.class,
                NextDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.UP, Direction.UP);
    }

    @Test
    void givenLegalDirection_thenNextDirectionUnchanged() {
        dominion.createEntity(
            new CurrentDirection(Direction.UP),
            new NextDirection(Direction.RIGHT)
        );
        dominion.createEntity(TurnStarted.INSTANCE);
        when(mode.legalTurns()).thenReturn(Set.of(LegalTurn.RIGHT));
        nextDirectionSystem.run(0);
        assertThat(
            dominion.findEntitiesWith(
                CurrentDirection.class,
                NextDirection.class
            )
        )
            .singleElement()
            .extracting(result -> result.comp1().value, result -> result.comp2().value)
            .containsExactly(Direction.UP, Direction.RIGHT);
    }
}
