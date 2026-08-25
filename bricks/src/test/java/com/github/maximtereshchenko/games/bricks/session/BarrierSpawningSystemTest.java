package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.bricks.configuration.Blueprints;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class BarrierSpawningSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> barrierEntities =
        registry.view(new Query().all(Barrier.class));
    private final Blueprints blueprints =
        new Blueprints.Builder(
            Map.of(BricksBlueprints.BARRIER, List.of(new Barrier(1)))
        ).build();
    private final BarrierSpawningSystem barrierSpawningSystem =
        new BarrierSpawningSystem(registry, blueprints);

    @BeforeEach
    void setUp() {
        registry.addSystems(barrierSpawningSystem);
    }

    @Test
    void givenActivatedSpawnBarrierBonusAndNoBarrier_thenBarrierSpawned() {
        registry.addComponents(
            registry.createEntity(),
            new SpawnBarrierBonus(2),
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(barrierEntities).hasSize(1);
    }

    @Test
    void givenExistingBarrier_thenBarrierNotSpawned() {
        registry.addComponents(registry.createEntity(), new Barrier(1));
        registry.addComponents(
            registry.createEntity(),
            new SpawnBarrierBonus(2),
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(barrierEntities).hasSize(1);
    }
}
