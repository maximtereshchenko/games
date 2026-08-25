package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentSpawningSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> segmentEntities =
        registry.view(new Query().all(Segment.class));
    private final Iterable<Entity> spawnedSegmentEntities =
        registry.view(
            new Query()
                .all(
                    Segment.class,
                    WorldPosition.class,
                    PaletteColor.class,
                    Opacity.class
                )
        );
    private final SegmentSpawningSystem segmentSpawningSystem =
        new SegmentSpawningSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(segmentSpawningSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0)
        );
        registry.addComponents(registry.createEntity(), new SegmentPolicy(1, 4));
        registry.update(0);
        assertThat(segmentEntities).isEmpty();
    }

    @Test
    void givenNoSegmentPolicy_thenNoSegmentSpawned() {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(segmentEntities).isEmpty();
    }

    @Test
    void givenTurnStartedEvent_thenSegmentSpawned() {
        registry.addComponents(
            registry.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0)
        );
        registry.addComponents(registry.createEntity(), new SegmentPolicy(1, 4));
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(spawnedSegmentEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(Segment.class),
                entity -> entity.component(WorldPosition.class),
                entity -> entity.component(PaletteColor.class),
                entity -> entity.component(Opacity.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new Segment(4),
                    new WorldPosition(0, 0),
                    PaletteColor.SEGMENT,
                    new Opacity(1)
                )
            );
    }
}
