package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

final class AppleSpawningSystemTest {

    private final Dominion dominion = Dominion.create();
    private final AppleSpawningSystem appleSpawningSystem = new AppleSpawningSystem(
        dominion,
        new Random(0),
        2
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        dominion.createEntity(new WorldDimensions(2, 2));
        var before = dominion.findAllEntities().stream().toList();
        appleSpawningSystem.run();
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenTurnStartedEvent_thenApplesSpawned() {
        dominion.createEntity(new WorldDimensions(2, 2));
        dominion.createEntity(TurnStarted.INSTANCE);
        appleSpawningSystem.run();
        assertThat(dominion.findEntitiesWith(Apple.class, Position.class, Visible.class))
            .allSatisfy(result -> assertThat(result.comp3().color()).isEqualTo(Colors.APPLE))
            .extracting(Results.With3::comp2)
            .containsExactlyInAnyOrder(new Position(0, 1), new Position(1, 1));
    }

    @Test
    void givenPosition_thenApplesSpawnedInFreeSpace() {
        dominion.createEntity(new WorldDimensions(2, 2));
        dominion.createEntity(new Position(0, 1));
        dominion.createEntity(TurnStarted.INSTANCE);
        appleSpawningSystem.run();
        assertThat(dominion.findEntitiesWith(Apple.class, Position.class, Visible.class))
            .allSatisfy(result -> assertThat(result.comp3().color()).isEqualTo(Colors.APPLE))
            .extracting(Results.With3::comp2)
            .containsExactlyInAnyOrder(new Position(1, 0), new Position(1, 1));
    }

    @Test
    void givenNotHasSpace_thenStopSpawningApples() {
        dominion.createEntity(new WorldDimensions(2, 2));
        dominion.createEntity(new Position(0, 0));
        dominion.createEntity(new Position(0, 1));
        dominion.createEntity(new Position(1, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        appleSpawningSystem.run();
        assertThat(dominion.findEntitiesWith(Apple.class, Position.class, Visible.class))
            .singleElement()
            .extracting(Results.With3::comp2, result -> result.comp3().color())
            .containsExactlyInAnyOrder(new Position(1, 1), Colors.APPLE);
    }
}