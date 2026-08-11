package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadCollisionSystemTest {

    private final World world = new World();
    private final Iterable<Entity> deadEntities =
        world.entities(new Query().all(Dead.class));
    private final Iterable<Entity> deadHeadEntities =
        world.entities(new Query().all(Head.class, Dead.class));
    private final HeadCollisionSystem headCollisionSystem =
        new HeadCollisionSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(headCollisionSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(0, 0);
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new WorldPositionIntent(intent)
        );
        world.addComponents(
            world.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(0, 0)
        );
        world.update(0);
        assertThat(deadEntities).isEmpty();
    }

    @Test
    void givenHeadIntentOnWall_thenDeadAdded() {
        var intent = new WorldPosition(0, 0);
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new WorldPositionIntent(intent)
        );
        world.addComponents(
            world.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(0, 0)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(deadHeadEntities).hasSize(1);
    }

    @Test
    void givenHeadIntentOnSegment_thenDeadAdded() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new WorldPositionIntent(intent)
        );
        world.addComponents(
            world.createEntity(),
            new Segment(1),
            new WorldPosition(1, 1)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(deadHeadEntities).hasSize(1);
    }

    @Test
    void givenNoCollision_thenNoDeadAdded() {
        var intent = new WorldPosition(0, 0);
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new WorldPositionIntent(intent)
        );
        world.addComponents(
            world.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(1, 1)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(deadEntities).isEmpty();
    }
}
