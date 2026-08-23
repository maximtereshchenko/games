package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Transform;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class RegistrySynchronizationSystemTest {

    private final Registry registry = new Registry();
    private final Fixture fixture = mock();
    private final Body body = mock();
    private final Transform transform = mock();
    private final RegistrySynchronizationSystem registrySynchronizationSystem =
        new RegistrySynchronizationSystem(registry);

    @BeforeEach
    void setUp() {
        registry.addSystems(registrySynchronizationSystem);
    }

    @Test
    void givenBody_thenPositionAndVelocityCopied() {
        when(fixture.getBody()).thenReturn(body);
        when(body.getTransform()).thenReturn(transform);
        when(transform.getPosition()).thenReturn(new Vector2(1, 2));
        when(body.getLinearVelocity()).thenReturn(new Vector2(3, 4));
        var worldPosition = new WorldPosition(new Vector2());
        var velocity = new Velocity(new Vector2());
        registry.addComponents(registry.createEntity(), fixture, worldPosition, velocity);
        registry.update(0);
        assertThat(worldPosition.vector2()).usingRecursiveComparison().isEqualTo(new Vector2(1, 2));
        assertThat(velocity.vector2()).usingRecursiveComparison().isEqualTo(new Vector2(3, 4));
    }
}
