package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class WarpedRelativeDirectionSystemTest {

    private final World world = new World();
    private final Iterable<Entity> directionIntentEntities =
        world.entities(
            new Query()
                .all(
                    Warped.class,
                    WarpedRelativeDirection.class,
                    Direction.class,
                    DirectionIntent.class
                )
        );
    private final WarpedRelativeDirectionSystem warpedRelativeDirectionSystem =
        new WarpedRelativeDirectionSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(warpedRelativeDirectionSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            Warped.INSTANCE,
            new WarpedRelativeDirection(RelativeDirection.LEFT),
            Direction.RIGHT,
            new DirectionIntent(Set.of(), Direction.RIGHT)
        );
        world.update(0);
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
        world.addComponents(
            world.createEntity(),
            Warped.INSTANCE,
            new WarpedRelativeDirection(RelativeDirection.LEFT),
            Direction.RIGHT,
            new DirectionIntent(Set.of(), Direction.RIGHT)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
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
