package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class BonusCollisionSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> activatedBonusEntities =
        registry.view(new Query().all(Bonus.class, Activated.class, Removed.class));
    private final BonusCollisionSystem bonusCollisionSystem =
        new BonusCollisionSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(bonusCollisionSystem);
    }

    @Test
    void givenMutualCollision_thenBonusActivatedAndRemoved() {
        var paddleId = registry.createEntity();
        var bonusId = registry.createEntity();
        registry.addComponents(paddleId, Paddle.INSTANCE, new Collisions(Set.of(bonusId)));
        registry.addComponents(bonusId, Bonus.INSTANCE, new Collisions(Set.of(paddleId)));
        registry.update(0);
        assertThat(activatedBonusEntities).hasSize(1);
    }
}
