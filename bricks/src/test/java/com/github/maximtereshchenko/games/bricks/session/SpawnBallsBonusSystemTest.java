package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SpawnBallsBonusSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> commandEntities =
        registry.view(new Query().all(SpawnBallCommand.class));
    private final SpawnBallsBonusSystem spawnBallsBonusSystem =
        new SpawnBallsBonusSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(spawnBallsBonusSystem);
    }

    @Test
    void givenActivatedSpawnBallsBonus_thenCommandsSpawnedAbovePaddle() {
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new WorldPosition(new Vector2(10, 5)),
            new Rectangle(5, 0.5f),
            new BallOffset(2)
        );
        registry.addComponents(
            registry.createEntity(),
            new SpawnBallsBonus(2),
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(commandEntities)
            .hasSize(2)
            .allSatisfy(entity ->
                assertThat(entity.component(SpawnBallCommand.class).worldPosition().vector2().y)
                    .isEqualTo(7)
            );
    }
}
