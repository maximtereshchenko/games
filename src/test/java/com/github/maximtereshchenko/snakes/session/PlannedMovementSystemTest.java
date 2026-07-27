package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class PlannedMovementSystemTest {

    private final World world = new World();
    private final PlannedMovementSystem plannedMovementSystem =
        new PlannedMovementSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(plannedMovementSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new ForwardMovement(1, 1, Direction.RIGHT),
            new PlannedMovement(Set.of(RelativeDirection.RIGHT), Direction.UP)
        );
        world.update(0);
        assertThat(
            world.entities(
                new Query().all(ForwardMovement.class, PlannedMovement.class)
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(ForwardMovement.class),
                entity -> entity.component(PlannedMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new ForwardMovement(1, 1, Direction.RIGHT),
                    new PlannedMovement(
                        Set.of(RelativeDirection.RIGHT),
                        Direction.UP
                    )
                )
            );
    }

    @Test
    void givenNonLegalDirection_thenPlannedMovementDirectionSetToForwardMovementDirection() {
        world.addComponents(
            world.createEntity(),
            new ForwardMovement(1, 1, Direction.RIGHT),
            new PlannedMovement(Set.of(RelativeDirection.RIGHT), Direction.UP)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query().all(ForwardMovement.class, PlannedMovement.class)
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(ForwardMovement.class),
                entity -> entity.component(PlannedMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new ForwardMovement(1, 1, Direction.RIGHT),
                    new PlannedMovement(
                        Set.of(RelativeDirection.RIGHT),
                        Direction.RIGHT
                    )
                )
            );
    }

    @Test
    void givenLegalDirection_thenForwardMovementDirectionSetToPlanned() {
        world.addComponents(
            world.createEntity(),
            new ForwardMovement(1, 1, Direction.RIGHT),
            new PlannedMovement(Set.of(RelativeDirection.RIGHT), Direction.DOWN)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query().all(ForwardMovement.class, PlannedMovement.class)
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(ForwardMovement.class),
                entity -> entity.component(PlannedMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new ForwardMovement(1, 1, Direction.DOWN),
                    new PlannedMovement(
                        Set.of(RelativeDirection.RIGHT),
                        Direction.DOWN
                    )
                )
            );
    }
}
