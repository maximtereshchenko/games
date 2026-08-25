package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class BonusResettingSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> removedBonusEntities =
        registry.view(new Query().all(Bonus.class, Removed.class));
    private final BonusResettingSystem bonusResettingSystem =
        new BonusResettingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(bonusResettingSystem);
    }

    @Test
    void givenNoBalls_thenBonusesRemoved() {
        registry.addComponents(registry.createEntity(), Bonus.INSTANCE);
        registry.update(0);
        assertThat(removedBonusEntities).hasSize(1);
    }

    @Test
    void givenBall_thenBonusesNotRemoved() {
        registry.addComponents(registry.createEntity(), Ball.INSTANCE);
        registry.addComponents(registry.createEntity(), Bonus.INSTANCE);
        registry.update(0);
        assertThat(removedBonusEntities).isEmpty();
    }
}
