package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class MultiplyBallsBonusSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> commandEntities =
        registry.entities(new Query().all(SpawnBallCommand.class));
    private final MultiplyBallsBonusSystem multiplyBallsBonusSystem =
        new MultiplyBallsBonusSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(multiplyBallsBonusSystem);
    }

    @Test
    void givenActivatedMultiplyBallsBonus_thenSpawnCommandsCreated() {
        registry.addComponents(
            registry.createEntity(),
            Ball.INSTANCE,
            new WorldPosition(new Vector2(1, 2)),
            new Velocity(new Vector2(0, 5))
        );
        registry.addComponents(
            registry.createEntity(),
            new MultiplyBallsBonus(2),
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(commandEntities).hasSize(1);
    }

    @Test
    void givenNoActivatedBonus_thenNoCommands() {
        registry.addComponents(
            registry.createEntity(),
            Ball.INSTANCE,
            new WorldPosition(new Vector2(1, 2)),
            new Velocity(new Vector2(0, 5))
        );
        registry.update(0);
        assertThat(commandEntities).isEmpty();
    }
}
