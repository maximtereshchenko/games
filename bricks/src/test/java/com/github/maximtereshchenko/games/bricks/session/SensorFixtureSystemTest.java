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

final class SensorFixtureSystemTest {

    private final Registry registry = new Registry();
    private final Iterable<Entity> fixtureEntities =
        registry.entities(new Query().all(Fixture.class));
    private final World world = mock();
    private final PhysicsObjectFactory physicsObjectFactory = mock();
    private final Fixture fixture = mock();
    private final SensorFixtureSystem sensorFixtureSystem = new SensorFixtureSystem(
        registry,
        world,
        physicsObjectFactory
    );

    @BeforeEach
    void setUp() {
        when(physicsObjectFactory.sensorFixture(
            any(), any(), any(WorldPosition.class), anyFloat()
        )).thenReturn(fixture);
        registry.addSystems(sensorFixtureSystem);
    }

    @Test
    void givenStar_thenSensorFixtureCreated() {
        registry.addComponents(
            registry.createEntity(),
            BodyDef.BodyType.DynamicBody,
            new WorldPosition(new Vector2()),
            Sensor.INSTANCE,
            new Star(2)
        );
        registry.update(0);
        assertThat(fixtureEntities)
            .singleElement()
            .extracting(entity -> entity.component(Fixture.class))
            .isSameAs(fixture);
        verify(physicsObjectFactory).sensorFixture(
            world,
            BodyDef.BodyType.DynamicBody,
            new WorldPosition(new Vector2()),
            2
        );
    }
}
