package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class WallPolicySystemTest {

    private final World world = new World();
    private final Iterable<Entity> wallEntities =
        world.entities(new Query().all(Wall.class));
    private final Iterable<Entity> spawnedWallEntities =
        world.entities(
            new Query()
                .all(
                    Wall.class,
                    WorldPosition.class,
                    Colored.class
                )
        );
    private final WallPolicySystem wallPolicySystem = new WallPolicySystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(wallPolicySystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), WallPolicy.INSTANCE);
        world.addComponents(
            world.createEntity(),
            new FoodConsumed(1),
            new WorldPosition(5, 5)
        );
        world.update(0);
        assertThat(wallEntities).isEmpty();
    }

    @Test
    void givenNoWallPolicy_thenNoWallsCreated() {
        world.addComponents(
            world.createEntity(),
            new FoodConsumed(1),
            new WorldPosition(5, 5)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(wallEntities).isEmpty();
    }

    @Test
    void givenWallPolicyAndFoodConsumed_thenWallCreated() {
        world.addComponents(world.createEntity(), WallPolicy.INSTANCE);
        world.addComponents(
            world.createEntity(),
            new FoodConsumed(1),
            new WorldPosition(5, 5)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(spawnedWallEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(WorldPosition.class),
                entity -> entity.component(Colored.class)
            )
            .containsExactly(
                new WorldPosition(5, 5),
                Colored.WALL
            );
    }
}
