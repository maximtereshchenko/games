package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class AppleEatingSystemTest {

    private final Dominion dominion = Dominion.create();
    private final AppleEatingSystem appleEatingSystem = new AppleEatingSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Apple.INSTANCE, new Position(0, 0));
        var before = dominion.findAllEntities().stream().toList();
        appleEatingSystem.run();
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenTurnStartedEvent_thenAppleEaten() {
        dominion.createEntity(Head.INSTANCE, new Position(0, 0));
        dominion.createEntity(Apple.INSTANCE, new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        appleEatingSystem.run();
        assertThat(dominion.findEntitiesWith(Apple.class, Position.class)).isEmpty();
        assertThat(dominion.findEntitiesWith(AppleEaten.class, Event.class)).hasSize(1);
    }

    @Test
    void givenHeadNotOnApple_thenNoAppleEaten() {
        dominion.createEntity(Head.INSTANCE, new Position(1, 0));
        dominion.createEntity(Apple.INSTANCE, new Position(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        appleEatingSystem.run();
        assertThat(dominion.findEntitiesWith(Apple.class, Position.class)).isNotEmpty();
        assertThat(dominion.findEntitiesWith(AppleEaten.class, Event.class)).isEmpty();
    }
}