package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.RIGHT)
        );
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        Head.class,
                        Position.class,
                        CurrentForwardDirection.class,
                        NextForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(Position.class),
                entity -> entity.component(CurrentForwardDirection.class).value,
                entity -> entity.component(NextForwardDirection.class).value
            )
            .containsExactly(
                new Position(1, 1),
                Direction.RIGHT,
                Direction.RIGHT
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
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.RIGHT)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        Head.class,
                        Position.class,
                        CurrentForwardDirection.class,
                        NextForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(Position.class),
                entity -> entity.component(CurrentForwardDirection.class).value,
                entity -> entity.component(NextForwardDirection.class).value
            )
            .containsExactly(
                new Position(0, 0),
                Direction.UP,
                Direction.UP
            );
    }
}