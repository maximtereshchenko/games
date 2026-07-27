package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class WarpSystemTest {

    private final World world = new World();
    private final WarpSystem warpSystem = new WarpSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(warpSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new Warp(new Position(0, 0), RelativeDirection.LEFT),
            HeadCollisionTarget.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new Position(1, 1),
            new ForwardMovement(1, 1, Direction.RIGHT)
        );
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        Head.class,
                        Position.class,
                        ForwardMovement.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(Position.class),
                entity -> entity.component(ForwardMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new Position(1, 1),
                    new ForwardMovement(1, 1, Direction.RIGHT)
                )
            );
    }

    @Test
    void givenWarpCollisionTarget_thenHeadPositionDirectionChanged() {
        world.addComponents(
            world.createEntity(),
            new Warp(new Position(0, 0), RelativeDirection.LEFT),
            HeadCollisionTarget.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            Head.INSTANCE,
            new Position(1, 1),
            new ForwardMovement(1, 1, Direction.RIGHT)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        Head.class,
                        Position.class,
                        ForwardMovement.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(Position.class),
                entity -> entity.component(ForwardMovement.class)
            )
            .usingRecursiveComparison()
            .isEqualTo(
                List.of(
                    new Position(0, 0),
                    new ForwardMovement(1, 1, Direction.UP)
                )
            );
    }
}