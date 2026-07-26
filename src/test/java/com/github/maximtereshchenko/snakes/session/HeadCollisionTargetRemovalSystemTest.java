package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class HeadCollisionTargetRemovalSystemTest {

    private final World world = new World();
    private final HeadCollisionTargetRemovalSystem headCollisionTargetRemovalSystem =
        new HeadCollisionTargetRemovalSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(headCollisionTargetRemovalSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new Position(0, 0),
            HeadCollisionTarget.INSTANCE
        );
        world.update(0);
        assertThat(
            world.entities(
                new Query().all(Position.class, HeadCollisionTarget.class))
        )
            .hasSize(1);
    }

    @Test
    void givenHeadCollisionTarget_thenTagRemoved() {
        world.addComponents(
            world.createEntity(),
            new Position(0, 0),
            HeadCollisionTarget.INSTANCE
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(
            world.entities(
                new Query().all(Position.class).none(HeadCollisionTarget.class)
            )
        )
            .hasSize(1);
    }
}