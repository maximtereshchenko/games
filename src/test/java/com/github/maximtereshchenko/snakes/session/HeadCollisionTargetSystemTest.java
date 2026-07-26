package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

final class HeadCollisionTargetSystemTest {

    private final World world = new World();
    private final HeadCollisionTargetSystem headCollisionTargetSystem =
        new HeadCollisionTargetSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(headCollisionTargetSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(world.createEntity(), Head.INSTANCE, new Position(0, 0));
        world.addComponents(world.createEntity(), new Position(0, 0));
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(Position.class)
                    .none(Head.class, HeadCollisionTarget.class))
        )
            .hasSize(1);
    }

    @Test
    void givenNoHeadOnPosition_thenNoChanges() {
        world.addComponents(world.createEntity(), Head.INSTANCE, new Position(1, 1));
        world.addComponents(world.createEntity(), new Position(0, 0));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query()
                    .all(Position.class)
                    .none(Head.class, HeadCollisionTarget.class)
            )
        )
            .hasSize(1);
    }

    @Test
    void givenHeadOnPosition_thenHeadCollisionTargetAdded() {
        world.addComponents(world.createEntity(), Head.INSTANCE, new Position(0, 0));
        world.addComponents(world.createEntity(), new Position(0, 0));
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query().all(Position.class, HeadCollisionTarget.class)
            )
        )
            .singleElement()
            .extracting(entity -> entity.component(Position.class))
            .isEqualTo(new Position(0, 0));
    }

    @Test
    void givenHeadCollisionTargetPresent_thenNoExceptionThrown() {
        world.addComponents(world.createEntity(), Head.INSTANCE, new Position(0, 0));
        world.addComponents(
            world.createEntity(),
            new Position(0, 0),
            HeadCollisionTarget.INSTANCE
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        assertThatCode(() -> world.update(0))
            .doesNotThrowAnyException();
    }
}