package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class TagRemovalSystemTest {

    private final World world = new World();
    private final Iterable<Entity> foodConsumedEntities =
        world.entities(new Query().all(FoodConsumed.class));
    private final Iterable<Entity> turnStartedEntities =
        world.entities(new Query().all(TurnStarted.class));
    private final Iterable<Entity> warpedEntities =
        world.entities(new Query().all(Warped.class));
    private final Iterable<Entity> remainingEntities =
        world.entities(new Query().all(Head.class));
    private final TagRemovalSystem tagRemovalSystem =
        new TagRemovalSystem(world, TurnStarted.class, FoodConsumed.class, Warped.class);

    @BeforeEach
    void setUp() {
        world.addSystems(tagRemovalSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), new FoodConsumed(1));
        world.update(0);
        assertThat(foodConsumedEntities).hasSize(1);
    }

    @Test
    void givenTags_thenTagsRemoved() {
        world.addComponents(
            world.createEntity(),
            TurnStarted.INSTANCE,
            new FoodConsumed(1),
            Warped.INSTANCE,
            Head.INSTANCE
        );
        world.update(0);
        assertThat(turnStartedEntities).isEmpty();
        assertThat(foodConsumedEntities).isEmpty();
        assertThat(warpedEntities).isEmpty();
        assertThat(remainingEntities).hasSize(1);
    }
}
