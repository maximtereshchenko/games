package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class FoodEatingSystemTest {

    private final World world = new World();
    private final EntityFactory entityFactory = mock();
    private final FoodEatingSystem foodEatingSystem =
        new FoodEatingSystem(world, entityFactory);

    @BeforeEach
    void setUp() {
        world.addSystems(foodEatingSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            Food.INSTANCE,
            HeadCollisionTarget.INSTANCE
        );
        world.update(0);
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnStartedEvent_thenFoodEaten() {
        world.addComponents(
            world.createEntity(),
            Food.INSTANCE,
            HeadCollisionTarget.INSTANCE
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Food.class))).isEmpty();
        verify(entityFactory).createFoodEatenEvent(any(WorldEdit.class));
    }
}