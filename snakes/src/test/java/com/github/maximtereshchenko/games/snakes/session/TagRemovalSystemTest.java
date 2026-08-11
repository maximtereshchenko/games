package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TagRemovalSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> foodConsumedEntities =
        registry.entities(new Query().all(FoodConsumed.class));
    private final Iterable<Entity> turnStartedEntities =
        registry.entities(new Query().all(TurnStarted.class));
    private final Iterable<Entity> warpedEntities =
        registry.entities(new Query().all(Warped.class));
    private final Iterable<Entity> remainingEntities =
        registry.entities(new Query().all(Head.class));
    private final TagRemovalSystem tagRemovalSystem =
        new TagRemovalSystem(registry, TurnStarted.class, FoodConsumed.class, Warped.class);

    @BeforeEach
    void setUp() {
        registry.addSystems(tagRemovalSystem);
    }

    @Test
    void givenTags_thenTagsRemoved() {
        registry.addComponents(
            registry.createEntity(),
            TurnStarted.INSTANCE,
            new FoodConsumed(1),
            Warped.INSTANCE,
            Head.INSTANCE
        );
        registry.update(0);
        assertThat(turnStartedEntities).isEmpty();
        assertThat(foodConsumedEntities).isEmpty();
        assertThat(warpedEntities).isEmpty();
        assertThat(remainingEntities).hasSize(1);
    }
}
