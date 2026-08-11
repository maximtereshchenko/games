package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class WallSpawningSystemTest {

    private final World world = new World();
    private final Iterable<Entity> wallEntities =
        world.entities(new Query().all(Wall.class));
    private final Iterable<Entity> spawnedWallEntities =
        world.entities(
            new Query()
                .all(
                    Wall.class,
                    WorldPosition.class,
                    PaletteColor.class,
                    Opacity.class
                )
        );
    private final WallSpawningSystem wallSpawningSystem = new WallSpawningSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(wallSpawningSystem);
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
                entity -> entity.component(PaletteColor.class),
                entity -> entity.component(Opacity.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new WorldPosition(5, 5),
                    PaletteColor.WALL,
                    new Opacity(1)
                )
            );
    }
}
