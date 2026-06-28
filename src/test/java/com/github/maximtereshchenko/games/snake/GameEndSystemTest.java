package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class GameEndSystemTest {

    private final Dominion dominion = Dominion.create();
    private final GameEndSystem gameEndSystem = new GameEndSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Game());
        gameEndSystem.run();
        assertThat(dominion.findCompositionsWith(Game.class))
            .extracting(game -> game.status)
            .containsExactly(Game.Status.RUNNING);
    }

    @Test
    void givenNoDuplicatePosition_thenGameRunning() {
        dominion.createEntity(new Game());
        dominion.createEntity(new Position(0, 0));
        dominion.createEntity(new Position(1, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        gameEndSystem.run();
        assertThat(dominion.findCompositionsWith(Game.class))
            .extracting(game -> game.status)
            .containsExactly(Game.Status.RUNNING);
    }

    @Test
    void givenDuplicatePosition_thenGameEnded() {
        dominion.createEntity(new Game());
        dominion.createEntity(new Position(0, 0));
        dominion.createEntity(new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        gameEndSystem.run();
        assertThat(dominion.findCompositionsWith(Game.class))
            .extracting(game -> game.status)
            .containsExactly(Game.Status.ENDED);
    }
}