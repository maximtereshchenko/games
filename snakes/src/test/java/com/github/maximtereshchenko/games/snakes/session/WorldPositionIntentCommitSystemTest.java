package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class WorldPositionIntentCommitSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> worldPositionEntities =
        registry.entities(new Query().all(WorldPosition.class));
    private final WorldPositionIntentCommitSystem worldPositionIntentCommitSystem =
        new WorldPositionIntentCommitSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(worldPositionIntentCommitSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        var intent = new WorldPosition(1, 1);
        registry.addComponents(
            registry.createEntity(),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        registry.update(0);
        assertThat(worldPositionEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPosition.class))
            .isEqualTo(new WorldPosition(0, 0));
    }

    @Test
    void givenTurnStartedEvent_thenPositionCopiedFromIntent() {
        var intent = new WorldPosition(1, 1);
        registry.addComponents(
            registry.createEntity(),
            new WorldPosition(0, 0),
            new WorldPositionIntent(intent)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(worldPositionEntities)
            .singleElement()
            .extracting(entity -> entity.component(WorldPosition.class))
            .isEqualTo(new WorldPosition(1, 1));
    }
}
