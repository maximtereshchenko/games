package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class WarpedRelativeDirectionSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> directionIntentEntities =
        registry.view(
            new Query()
                .all(
                    Warped.class,
                    WarpedRelativeDirection.class,
                    Direction.class,
                    DirectionIntent.class
                )
        );
    private final WarpedRelativeDirectionSystem warpedRelativeDirectionSystem =
        new WarpedRelativeDirectionSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(warpedRelativeDirectionSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        registry.addComponents(
            registry.createEntity(),
            Warped.INSTANCE,
            new WarpedRelativeDirection(RelativeDirection.LEFT),
            Direction.RIGHT,
            new DirectionIntent(Set.of(), Direction.RIGHT)
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
                    new DirectionIntent(Set.of(), Direction.RIGHT)
                )
            );
    }

    @Test
    void givenWarped_thenDirectionAndIntentUpdated() {
        registry.addComponents(
            registry.createEntity(),
            Warped.INSTANCE,
            new WarpedRelativeDirection(RelativeDirection.LEFT),
            Direction.RIGHT,
            new DirectionIntent(Set.of(), Direction.RIGHT)
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
                    Direction.UP,
                    new DirectionIntent(Set.of(), Direction.UP)
                )
            );
    }
}
