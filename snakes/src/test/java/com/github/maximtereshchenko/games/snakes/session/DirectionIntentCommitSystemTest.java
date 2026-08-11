package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class DirectionIntentCommitSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> directionIntentEntities =
        registry.entities(new Query().all(Direction.class, DirectionIntent.class));
    private final DirectionIntentCommitSystem directionIntentCommitSystem =
        new DirectionIntentCommitSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(directionIntentCommitSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            Direction.RIGHT,
            new DirectionIntent(Set.of(RelativeDirection.RIGHT), Direction.DOWN)
        );
        registry.update(0);
        assertThat(directionIntentEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(Direction.class),
                entity -> entity.component(DirectionIntent.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    Direction.RIGHT,
                    new DirectionIntent(
                        Set.of(RelativeDirection.RIGHT),
                        Direction.DOWN
                    )
                )
            );
    }

    @Test
    void givenTurnStartedEvent_thenDirectionSetToIntent() {
        registry.addComponents(
            registry.createEntity(),
            Direction.RIGHT,
            new DirectionIntent(Set.of(RelativeDirection.RIGHT), Direction.DOWN)
        );
        registry.addComponents(registry.createEntity(), TurnStarted.INSTANCE);
        registry.update(0);
        assertThat(directionIntentEntities)
            .singleElement()
            .extracting(
                entity -> entity.component(Direction.class),
                entity -> entity.component(DirectionIntent.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    Direction.DOWN,
                    new DirectionIntent(
                        Set.of(RelativeDirection.RIGHT),
                        Direction.DOWN
                    )
                )
            );
    }
}
