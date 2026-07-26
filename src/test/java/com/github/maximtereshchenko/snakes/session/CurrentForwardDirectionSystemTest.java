package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class CurrentForwardDirectionSystemTest {

    private final World world = new World();
    private final CurrentForwardDirectionSystem currentDirectionSystem =
        new CurrentForwardDirectionSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(currentDirectionSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.UP)
        );
        world.update(0);
        assertThat(
            world.entities(new Query().all(
                CurrentForwardDirection.class,
                NextForwardDirection.class
            ))
        )
            .singleElement()
            .extracting(
                entity -> entity.component(CurrentForwardDirection.class).value,
                entity -> entity.component(NextForwardDirection.class).value
            )
            .containsExactly(Direction.RIGHT, Direction.UP);
    }

    @Test
    void givenTurnStartedEvent_thenCurrentDirectionSetToNext() {
        world.addComponents(
            world.createEntity(),
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.UP)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(new Query().all(
                CurrentForwardDirection.class,
                NextForwardDirection.class
            ))
        )
            .singleElement()
            .extracting(
                entity -> entity.component(CurrentForwardDirection.class).value,
                entity -> entity.component(NextForwardDirection.class).value
            )
            .containsExactly(Direction.UP, Direction.UP);
    }
}