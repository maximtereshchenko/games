package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class DirectionIntentSystemTest {

    private final World world = new World();
    private final Iterable<Entity> directionIntentEntities =
        world.entities(new Query().all(Direction.class, DirectionIntent.class));
    private final DirectionIntentSystem directionIntentSystem =
        new DirectionIntentSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(directionIntentSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            Direction.RIGHT,
            new DirectionIntent(Set.of(RelativeDirection.RIGHT), Direction.UP)
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
                    new DirectionIntent(Set.of(RelativeDirection.RIGHT), Direction.UP)
                )
            );
    }

    @Test
    void givenNonLegalDirection_thenDirectionIntentSetToCurrentDirection() {
        world.addComponents(
            world.createEntity(),
            Direction.RIGHT,
            new DirectionIntent(Set.of(RelativeDirection.RIGHT), Direction.UP)
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
                    Direction.RIGHT,
                    new DirectionIntent(
                        Set.of(RelativeDirection.RIGHT),
                        Direction.RIGHT
                    )
                )
            );
    }

    @Test
    void givenLegalDirection_thenDirectionIntentUnchanged() {
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
                    Direction.RIGHT,
                    new DirectionIntent(
                        Set.of(RelativeDirection.RIGHT),
                        Direction.DOWN
                    )
                )
            );
    }
}
