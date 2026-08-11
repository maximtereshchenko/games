package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadCollisionSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> deadEntities =
        registry.entities(new Query().all(Dead.class));
    private final Iterable<Entity> deadHeadEntities =
        registry.entities(new Query().all(Head.class, Dead.class));
    private final HeadCollisionSystem headCollisionSystem =
        new HeadCollisionSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(headCollisionSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(0, 0);
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPositionIntent(intent)
        );
        registry.addComponents(
            registry.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(0, 0)
        );
        registry.update(0);
        assertThat(deadEntities).isEmpty();
    }

    @Test
    void givenHeadIntentOnWall_thenDeadAdded() {
        var intent = new WorldPosition(0, 0);
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPositionIntent(intent)
        );
        registry.addComponents(
            registry.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(0, 0)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(deadHeadEntities).hasSize(1);
    }

    @Test
    void givenHeadIntentOnSegment_thenDeadAdded() {
        var intent = new WorldPosition(1, 1);
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPositionIntent(intent)
        );
        registry.addComponents(
            registry.createEntity(),
            new Segment(1),
            new WorldPosition(1, 1)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(deadHeadEntities).hasSize(1);
    }

    @Test
    void givenNoCollision_thenNoDeadAdded() {
        var intent = new WorldPosition(0, 0);
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPositionIntent(intent)
        );
        registry.addComponents(
            registry.createEntity(),
            Wall.INSTANCE,
            new WorldPosition(1, 1)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(deadEntities).isEmpty();
    }
}
