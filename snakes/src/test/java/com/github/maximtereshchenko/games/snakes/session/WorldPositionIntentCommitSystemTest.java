package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class WorldPositionIntentCommitSystemTest {

    private final World world = new World();
    private final Iterable<Entity> worldPositionEntities =
        world.entities(new Query().all(WorldPosition.class));
    private final WorldPositionIntentCommitSystem worldPositionIntentCommitSystem =
        new WorldPositionIntentCommitSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(worldPositionIntentCommitSystem);
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
