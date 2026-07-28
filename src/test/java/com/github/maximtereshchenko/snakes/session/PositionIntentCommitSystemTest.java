package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class PositionIntentCommitSystemTest {

    private final World world = new World();
    private final Iterable<Entity> worldPositionEntities =
        world.entities(new Query().all(WorldPosition.class));
    private final PositionIntentCommitSystem positionIntentCommitSystem =
        new PositionIntentCommitSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(positionIntentCommitSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        world.update(0);
        assertThat(worldPositionEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPosition.class))
            .isEqualTo(new WorldPosition(0, 0));
    }

    @Test
    void givenTurnStartedEvent_thenPositionCopiedFromIntent() {
        var intent = new WorldPosition(1, 1);
        world.addComponents(
            world.createEntity(),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(worldPositionEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPosition.class))
            .isEqualTo(new WorldPosition(1, 1));
    }
}
