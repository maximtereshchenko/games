package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class WallSpawningSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> wallEntities =
        registry.entities(new Query().all(Wall.class));
    private final Iterable<Entity> spawnedWallEntities =
        registry.entities(
            new Query()
                .all(
                    Wall.class,
                    WorldPosition.class,
                    PaletteColor.class,
                    Opacity.class
                )
        );
    private final WallSpawningSystem wallSpawningSystem = new WallSpawningSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(wallSpawningSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(registry.createEntity(), WallPolicy.INSTANCE);
        registry.addComponents(
            registry.createEntity(),
            new FoodConsumed(1),
            new WorldPosition(5, 5)
        );
        registry.update(0);
        assertThat(wallEntities).isEmpty();
    }

    @Test
    void givenNoWallPolicy_thenNoWallsCreated() {
        registry.addComponents(
            registry.createEntity(),
            new FoodConsumed(1),
            new WorldPosition(5, 5)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(wallEntities).isEmpty();
    }

    @Test
    void givenWallPolicyAndFoodConsumed_thenWallCreated() {
        registry.addComponents(registry.createEntity(), WallPolicy.INSTANCE);
        registry.addComponents(
            registry.createEntity(),
            new FoodConsumed(1),
            new WorldPosition(5, 5)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
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
