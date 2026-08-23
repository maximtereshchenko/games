package com.github.maximtereshchenko.games.bricks.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class ComponentRemovalSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> collisionEntities =
        registry.entities(new Query().all(Collisions.class));
    private final Iterable<Entity> remainingEntities =
        registry.entities(new Query().all(Paddle.class));
    private final ComponentRemovalSystem componentRemovalSystem =
        new ComponentRemovalSystem(registry, Collisions.class);

    @BeforeEach
    void setUp() {
        registry.addSystems(componentRemovalSystem);
    }

    @Test
    void givenComponents_thenListedTypesRemoved() {
        registry.addComponents(
            registry.createEntity(),
            Paddle.INSTANCE,
            new Collisions(Set.of(1))
        );
        registry.update(0);
        assertThat(collisionEntities).isEmpty();
        assertThat(remainingEntities).hasSize(1);
    }
}
