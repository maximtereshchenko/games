package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class RectangleFixtureSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> fixtureEntities =
        registry.view(new Query().all(Fixture.class));
    private final World world = mock();
    private final PhysicsObjectFactory physicsObjectFactory = mock();
    private final Fixture fixture = mock();
    private final RectangleFixtureSystem rectangleFixtureSystem =
        new RectangleFixtureSystem(registry, world, physicsObjectFactory);

    @BeforeEach
    void setUp() {
        when(physicsObjectFactory.fixture(
            any(), any(), any(WorldPosition.class), any(Rectangle.class)
        )).thenReturn(fixture);
        registry.addSystems(rectangleFixtureSystem);
    }

    @Test
    void givenRectangle_thenFixtureCreatedWithEntityId() {
        var id = registry.createEntity();
        registry.addComponents(
            id,
            BodyDef.BodyType.DynamicBody,
            new WorldPosition(new Vector2(1, 2)),
            new Rectangle(1, 1)
        );
        registry.update(0);
        assertThat(fixtureEntities)
            .singleElement()
            .extracting(entity -> entity.component(Fixture.class).getUserData())
            .isNull();
        verify(fixture).setUserData(id);
    }
}
