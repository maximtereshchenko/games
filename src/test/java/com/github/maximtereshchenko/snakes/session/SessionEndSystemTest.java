package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class SessionEndSystemTest {

    private final World world = new World();
    private final SessionEndSystem sessionEndSystem =
        new SessionEndSystem(world);

    @BeforeEach
    void setUp() {
        world.addSystems(sessionEndSystem);
    }

    @Test
    void givenNoTurnStartedEvent_thenNoChanges() {
        world.addComponents(
            world.createEntity(),
            new Segment(1),
            HeadCollisionTarget.INSTANCE
        );
        world.addComponents(
            world.createEntity(),
            new Session(Session.Status.RUNNING)
        );
        world.update(0);
        assertThat(world.entities(new Query().all(Session.class)))
            .extracting(entity -> entity.component(Session.class).status)
            .containsExactly(Session.Status.RUNNING);
    }

    @Test
    void givenCollision_thenSessionEnded() {
        world.addComponents(
            world.createEntity(),
            new Session(Session.Status.RUNNING)
        );
        world.addComponents(
            world.createEntity(),
            new Segment(1),
            HeadCollisionTarget.INSTANCE
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Session.class)))
            .extracting(entity -> entity.component(Session.class).status)
            .containsExactly(Session.Status.ENDED);
    }

    @Test
    void givenNoAir_thenSessionEnded() {
        world.addComponents(
            world.createEntity(),
            new Session(Session.Status.RUNNING)
        );
        world.addComponents(
            world.createEntity(),
            new AirCounter(1, 0)
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Session.class)))
            .extracting(entity -> entity.component(Session.class).status)
            .containsExactly(Session.Status.ENDED);
    }

    @Test
    void givenWallCollision_thenSessionEnded() {
        world.addComponents(
            world.createEntity(),
            new Session(Session.Status.RUNNING)
        );
        world.addComponents(
            world.createEntity(),
            Wall.INSTANCE,
            HeadCollisionTarget.INSTANCE
        );
        world.addComponents(world.createEntity(), TurnStarted.INSTANCE);
        world.update(0);
        assertThat(world.entities(new Query().all(Session.class)))
            .extracting(entity -> entity.component(Session.class).status)
            .containsExactly(Session.Status.ENDED);
    }
}