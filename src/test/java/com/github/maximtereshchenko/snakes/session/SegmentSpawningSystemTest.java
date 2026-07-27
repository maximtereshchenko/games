package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.captor;
import static org.mockito.Mockito.*;

final class SegmentSpawningSystemTest {

    private final World world = new World();
    private final EntityFactory entityFactory = mock();
    private final SegmentSpawningSystem segmentSpawningSystem =
        new SegmentSpawningSystem(world, entityFactory);
    private final ArgumentCaptor<SegmentDefinition> segmentDefinitionCaptor = captor();

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
                segmentDefinitionCaptor.capture(),
                eq(new Position(1, 1))
            );
        assertThat(segmentDefinitionCaptor.getValue())
            .usingRecursiveComparison()
            .isEqualTo(new SegmentDefinition(1, 1));
    }
}