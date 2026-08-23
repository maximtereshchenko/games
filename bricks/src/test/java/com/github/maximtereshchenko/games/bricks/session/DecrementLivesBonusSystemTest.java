package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class DecrementLivesBonusSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> commandEntities =
        registry.entities(new Query().all(Lives.class, DecrementLivesCommand.class));
    private final DecrementLivesBonusSystem decrementLivesBonusSystem =
        new DecrementLivesBonusSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(decrementLivesBonusSystem);
    }

    @Test
    void givenActivatedDecrementLivesBonus_thenDecrementCommandAdded() {
        registry.addComponents(registry.createEntity(), new Lives(3));
        registry.addComponents(
            registry.createEntity(),
            DecrementLivesBonus.INSTANCE,
            Activated.INSTANCE
        );
        registry.update(0);
        assertThat(commandEntities).hasSize(1);
    }
}
