package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

final class PhysicsSynchronizationSystemTest {

    private final Registry registry = new Registry();
    private final Fixture fixture = mock();
    private final Body body = mock();
    private final PhysicsSynchronizationSystem physicsSynchronizationSystem =
        new PhysicsSynchronizationSystem(registry);

    @BeforeEach
    void setUp() {
        when(fixture.getBody()).thenReturn(body);
        registry.addSystems(physicsSynchronizationSystem);
    }

    @Test
    void givenVelocity_thenBodyLinearVelocityUpdated() {
        var velocity = new Vector2(3, 4);
        registry.addComponents(
            registry.createEntity(),
            fixture,
            new Velocity(velocity)
        );
        registry.update(0);
        verify(body).setLinearVelocity(velocity);
    }
}
