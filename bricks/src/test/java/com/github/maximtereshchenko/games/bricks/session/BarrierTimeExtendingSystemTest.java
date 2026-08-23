package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class BarrierTimeExtendingSystemTest {

    private final Registry registry = new Registry();
    private final BarrierTimeExtendingSystem barrierTimeExtendingSystem =
        new BarrierTimeExtendingSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(barrierTimeExtendingSystem);
    }

    @Test
    void givenActivatedSpawnBarrierBonus_thenBarrierTimeExtended() {
        var barrier = new Barrier(1);
        registry.addComponents(registry.createEntity(), barrier);
        registry.addComponents(
            registry.createEntity(),
            new SpawnBarrierBonus(2),
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(barrier.remainingTimeSeconds).isEqualTo(3);
    }
}
