package com.github.maximtereshchenko.games.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.dominion.ecs.api.Dominion;
import dev.dominion.ecs.api.Results;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

final class AppleSpawningSystemTest {

    private final Dominion dominion = Dominion.create();
    private final AppleSpawningSystem appleSpawningSystem = new AppleSpawningSystem(
        dominion,
        new Random(0),
        new FitViewport(2, 2),
        2
    );

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var before = dominion.findAllEntities().stream().toList();
        appleSpawningSystem.run();
        assertThat(dominion.findAllEntities()).containsExactlyElementsOf(before);
    }

    @Test
    void givenTurnStartedEvent_thenApplesSpawned() {
        dominion.createEntity(TurnStarted.INSTANCE);
        appleSpawningSystem.run();
        assertThat(dominion.findEntitiesWith(Apple.class, Point.class, Color.class))
            .allSatisfy(result -> assertThat(result.comp3()).isEqualTo(Colors.APPLE))
            .extracting(Results.With3::comp2)
            .containsExactlyInAnyOrder(new Point(0, 1), new Point(1, 1));
    }

    @Test
    void givenPoint_thenApplesSpawnedInFreeSpace() {
        dominion.createEntity(new Point(0, 1));
        dominion.createEntity(TurnStarted.INSTANCE);
        appleSpawningSystem.run();
        assertThat(dominion.findEntitiesWith(Apple.class, Point.class, Color.class))
            .allSatisfy(result -> assertThat(result.comp3()).isEqualTo(Colors.APPLE))
            .extracting(Results.With3::comp2)
            .containsExactlyInAnyOrder(new Point(1, 0), new Point(1, 1));
    }

    @Test
    void givenNotEnoughSpace_thenStopSpawningApples() {
        dominion.createEntity(new Point(0, 0));
        dominion.createEntity(new Point(0, 1));
        dominion.createEntity(new Point(1, 0));
        dominion.createEntity(TurnStarted.INSTANCE);
        appleSpawningSystem.run();
        assertThat(dominion.findEntitiesWith(Apple.class, Point.class, Color.class))
            .singleElement()
            .extracting(Results.With3::comp2, Results.With3::comp3)
            .containsExactlyInAnyOrder(new Point(1, 1), Colors.APPLE);
    }
}