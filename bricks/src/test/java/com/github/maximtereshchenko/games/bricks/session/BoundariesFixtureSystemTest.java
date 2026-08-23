package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class BoundariesFixtureSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> boundaryEntities =
        registry.entities(new Query().all(Boundaries.class, Fixture.class));
    private final World world = mock();
    private final PhysicsObjectFactory physicsObjectFactory = mock();
    private final Fixture fixture = mock();
    private final BoundariesFixtureSystem boundariesFixtureSystem =
        new BoundariesFixtureSystem(
            registry,
            physicsObjectFactory,
            world
        );

    @BeforeEach
    void setUp() {
        when(physicsObjectFactory.boundariesFixture(world)).thenReturn(fixture);
        registry.addSystems(boundariesFixtureSystem);
    }

    @Test
    void givenNoBoundaries_thenBoundariesCreated() {
        registry.update(0);
        assertThat(boundaryEntities).hasSize(1);
        verify(fixture).setUserData(anyInt());
    }

    @Test
    void givenBoundaries_thenBoundariesNotCreatedAgain() {
        registry.update(0);
        registry.update(0);
        assertThat(boundaryEntities).hasSize(1);
    }
}
