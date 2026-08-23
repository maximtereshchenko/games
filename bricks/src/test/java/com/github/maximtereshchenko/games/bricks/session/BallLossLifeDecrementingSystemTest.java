package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class BallLossLifeDecrementingSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> commandEntities =
        registry.entities(new Query().all(Lives.class, DecrementLivesCommand.class));
    private final BallLossLifeDecrementingSystem ballLossLifeDecrementingSystem =
        new BallLossLifeDecrementingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(ballLossLifeDecrementingSystem);
    }

    @Test
    void givenNoBalls_thenDecrementCommandAdded() {
        registry.addComponents(registry.createEntity(), new Lives(3));
        registry.update(0);
        assertThat(commandEntities).hasSize(1);
    }

    @Test
    void givenBall_thenDecrementCommandNotAdded() {
        registry.addComponents(registry.createEntity(), new Lives(3));
        registry.addComponents(registry.createEntity(), Ball.INSTANCE);
        registry.update(0);
        assertThat(commandEntities).isEmpty();
    }
}
