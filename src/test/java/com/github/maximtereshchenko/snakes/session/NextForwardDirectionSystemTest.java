package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

final class NextForwardDirectionSystemTest {

    private final World world = new World();
    private final NextForwardDirectionSystem nextDirectionSystem =
        new NextForwardDirectionSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(nextDirectionSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new CurrentForwardDirection(Direction.RIGHT),
            new NextForwardDirection(Direction.UP),
            new LegalRelativeDirections(Set.of(RelativeDirection.RIGHT))
        );
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        CurrentForwardDirection.class,
                        NextForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(CurrentForwardDirection.class).value,
                entity -> entity.component(NextForwardDirection.class).value
            )
            .containsExactly(Direction.RIGHT, Direction.UP);
    }

    @Test
    void givenNonLegalDirection_thenNextDirectionRevertedToCurrent() {
        world.addComponents(
            world.createEntity(),
            new CurrentForwardDirection(Direction.UP),
            new NextForwardDirection(Direction.RIGHT),
            new LegalRelativeDirections(Set.of(RelativeDirection.LEFT))
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        CurrentForwardDirection.class,
                        NextForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(CurrentForwardDirection.class).value,
                entity -> entity.component(NextForwardDirection.class).value
            )
            .containsExactly(Direction.UP, Direction.UP);
    }

    @Test
    void givenLegalDirection_thenNextDirectionUnchanged() {
        world.addComponents(
            world.createEntity(),
            new CurrentForwardDirection(Direction.UP),
            new NextForwardDirection(Direction.RIGHT),
            new LegalRelativeDirections(Set.of(RelativeDirection.RIGHT))
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(
                        CurrentForwardDirection.class,
                        NextForwardDirection.class
                    )
            )
        )
            .singleElement()
            .extracting(
                entity -> entity.component(CurrentForwardDirection.class).value,
                entity -> entity.component(NextForwardDirection.class).value
            )
            .containsExactly(Direction.UP, Direction.RIGHT);
    }
}
