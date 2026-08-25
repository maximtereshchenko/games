package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class FixtureWidthUpdatingSystemTest {

    private final Registry registry = new Registry();
    private final World world = mock();
    private final PhysicsObjectFactory physicsObjectFactory = mock();
    private final Fixture fixture = mock();
    private final Fixture replacement = mock();
    private final Body body = mock();
    private final FixtureWidthUpdatingSystem fixtureWidthUpdatingSystem =
        new FixtureWidthUpdatingSystem(registry, world, physicsObjectFactory);

    @BeforeEach
    void setUp() {
        registry.addSystems(fixtureWidthUpdatingSystem);
    }

    @Test
    void givenSameWidth_thenBodyNotReplaced() {
        var rectangle = new Rectangle(5, 1);
        var id = registry.createEntity();
        registry.addComponents(
            id,
            new UpdateWidthCommand(5),
            fixture,
            rectangle,
            new WorldPosition(new Vector2())
        );
        registry.update(0);
        assertThat(registry.view(new com.github.maximtereshchenko.games.ecs.Query().all(Fixture.class))
            .iterator()
            .next()
            .component(Fixture.class)).isSameAs(fixture);
        verifyNoInteractions(world, physicsObjectFactory);
    }

    @Test
    void givenDifferentWidth_thenBodyReplaced() {
        var rectangle = new Rectangle(5, 1);
        var worldPosition = new WorldPosition(new Vector2(1, 2));
        when(fixture.getBody()).thenReturn(body);
        when(body.getType()).thenReturn(BodyDef.BodyType.KinematicBody);
        when(physicsObjectFactory.fixture(
            world,
            BodyDef.BodyType.KinematicBody,
            worldPosition,
            rectangle
        )).thenReturn(replacement);
        var id = registry.createEntity();
        registry.addComponents(
            id,
            new UpdateWidthCommand(8),
            fixture,
            rectangle,
            worldPosition
        );
        registry.update(0);
        var storedFixture = registry.view(
                new com.github.maximtereshchenko.games.ecs.Query().all(Fixture.class)
            )
            .iterator()
            .next()
            .component(Fixture.class);
        assertThat(storedFixture).isSameAs(replacement);
        assertThat(rectangle.halfWidth).isEqualTo(8);
        verify(replacement).setUserData(id);
        verify(world).destroyBody(body);
    }
}
