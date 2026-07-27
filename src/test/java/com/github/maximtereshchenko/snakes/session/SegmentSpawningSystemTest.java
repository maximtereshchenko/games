package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class SegmentSpawningSystemTest {

    private final World world = new World();
    private final EntityFactory entityFactory = mock();
    private final SegmentSpawningSystem segmentSpawningSystem =
        new SegmentSpawningSystem(world, entityFactory);

    @BeforeEach
    void setUp() {
        world.addSystems(segmentSpawningSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new Position(0, 0)
        );
        world.addComponents(world.createEntity(), new SegmentDefinition(0, 0));
        world.update(0);
        verifyNoInteractions(entityFactory);
    }

    @Test
    void givenTurnStartedEvent_thenSegmentSpawned() {
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new Position(1, 1)
        );
        world.addComponents(world.createEntity(), new SegmentDefinition(1, 1));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        verify(entityFactory)
            .createSegment(
                any(WorldEdit.class),
                eq(new Position(1, 1)),
                eq(1)
            );
    }
}