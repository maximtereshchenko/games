package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.assertj.core.api.Assertions.assertThat;

final class AppleEatingSystemTest {

    private final Dominion dominion = Dominion.create();
    private final AppleEatingSystem appleEatingSystem = new AppleEatingSystem(dominion);

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new Head(Head.Direction.RIGHT), new Point(0, 0));
        dominion.createEntity(Apple.INSTANCE, new Point(0, 0));
        var before = dominion.findAllEntities().stream().toList();
        appleEatingSystem.run();
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenTurnStartedEvent_thenAppleEaten() {
        dominion.createEntity(new Head(Head.Direction.RIGHT), new Point(0, 0));
        dominion.createEntity(Apple.INSTANCE, new Point(0, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        appleEatingSystem.run();
        assertThat(dominion.findEntitiesWith(Apple.class, Point.class)).isEmpty();
        assertThat(dominion.findEntitiesWith(AppleEaten.class, Event.class)).hasSize(1);
    }
}