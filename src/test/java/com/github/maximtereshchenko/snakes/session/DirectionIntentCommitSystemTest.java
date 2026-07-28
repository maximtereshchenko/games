package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class DirectionIntentCommitSystemTest {

    private final World world = new World();
    private final Iterable<Entity> directionIntentEntities =
        world.entities(new Query().all(Direction.class, DirectionIntent.class));
    private final DirectionIntentCommitSystem directionIntentCommitSystem =
        new DirectionIntentCommitSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(directionIntentCommitSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            Direction.RIGHT,
            new DirectionIntent(Set.of(RelativeDirection.RIGHT), Direction.DOWN)
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
                    new DirectionIntent(
                        Set.of(RelativeDirection.RIGHT),
                        Direction.DOWN
                    )
                )
            );
    }

    @Test
    void givenTurnStartedEvent_thenDirectionSetToIntent() {
        world.addComponents(
            world.createEntity(),
            Direction.RIGHT,
            new DirectionIntent(Set.of(RelativeDirection.RIGHT), Direction.DOWN)
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
                    Direction.DOWN,
                    new DirectionIntent(
                        Set.of(RelativeDirection.RIGHT),
                        Direction.DOWN
                    )
                )
            );
    }
}
