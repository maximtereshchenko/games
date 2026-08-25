package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class BarrierRemovalSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> removedBarrierEntities =
        registry.view(new Query().all(Barrier.class, Removed.class));
    private final BarrierRemovalSystem barrierRemovalSystem =
        new BarrierRemovalSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(barrierRemovalSystem);
    }

    @Test
    void givenRemainingTime_thenTimeDecreased() {
        var barrier = new Barrier(2);
        registry.addComponents(registry.createEntity(), barrier);
        registry.update(1);
        assertThat(barrier.remainingTimeSeconds).isEqualTo(1);
        assertThat(removedBarrierEntities).isEmpty();
    }

    @Test
    void givenTimeExpired_thenBarrierRemoved() {
        registry.addComponents(registry.createEntity(), new Barrier(0.5f));
        registry.update(1);
        assertThat(removedBarrierEntities).hasSize(1);
    }
}
