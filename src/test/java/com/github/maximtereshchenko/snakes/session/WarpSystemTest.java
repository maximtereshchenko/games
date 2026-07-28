package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class WarpSystemTest {

    private final World world = new World();
    private final Iterable<Entity> worldPositionIntentEntities =
        world.entities(new Query().all(WorldPositionIntent.class));
    private final Iterable<Entity> warpedEntities =
        world.entities(new Query().all(Warped.class));
    private final Iterable<Entity> warpedWorldPositionIntentEntities =
        world.entities(new Query().all(WorldPositionIntent.class, Warped.class));
    private final WarpSystem warpSystem = new WarpSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(warpSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            new Warp(new WorldPosition(0, 0)),
            new WorldPosition(1, 1)
        );
        world.addComponents(
            world.createEntity(),
            new WorldPositionIntent(intent)
        );
        world.update(0);
        assertThat(worldPositionIntentEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPositionIntent.class).value)
            .usingRecursiveComparison()
            .isEqualTo(new WorldPosition(1, 1));
        assertThat(warpedEntities).isEmpty();
    }

    @Test
    void givenWarpCollision_thenIntentMovedAndWarpedAdded() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            new Warp(new WorldPosition(0, 0)),
            new WorldPosition(1, 1)
        );
        world.addComponents(
            world.createEntity(),
            new WorldPositionIntent(intent)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(warpedWorldPositionIntentEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPositionIntent.class).value)
            .usingRecursiveComparison()
            .isEqualTo(new WorldPosition(0, 0));
    }
}
