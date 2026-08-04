package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class SegmentSpawningSystemTest {

    private final World world = new World();
    private final Iterable<Entity> segmentEntities =
        world.entities(new Query().all(Segment.class));
    private final Iterable<Entity> spawnedSegmentEntities =
        world.entities(
            new Query()
                .all(
                    Segment.class,
                    WorldPosition.class,
                    PaletteColor.class,
                    Opacity.class
                )
        );
    private final SegmentSpawningSystem segmentSpawningSystem =
        new SegmentSpawningSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(segmentSpawningSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        world.addComponents(world.createEntity(), new SegmentPolicy(1, 4));
        world.update(0);
        assertThat(segmentEntities).isEmpty();
    }

    @Test
    void givenHeadMoved_thenSegmentSpawned() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        world.addComponents(world.createEntity(), new SegmentPolicy(1, 4));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
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

    @Test
    void givenHeadNotMoved_thenNoSegmentSpawned() {
        var intent = new WorldPosition(0, 0);
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        world.addComponents(world.createEntity(), new SegmentPolicy(1, 4));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(segmentEntities).isEmpty();
    }
}
