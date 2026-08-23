package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.github.maximtereshchenko.games.bricks.configuration.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

final class PhysicsObjectFactoryTest {

    private final Configuration configuration = mock();
    private final World world = mock();
    private final Body body = mock();
    private final Fixture fixture = mock();
    private final Body first = mock();
    private final Body second = mock();
    private final WeldJoint weldJoint = mock();
    private final PhysicsObjectFactory physicsObjectFactory =
        new PhysicsObjectFactory(configuration);

    @BeforeEach
    void setUp() {
        when(world.createBody(any())).thenReturn(body);
        when(body.createFixture(any(FixtureDef.class))).thenReturn(fixture);
    }

    @Test
    void whenRectangleFixture_thenBodyConfigured() {
        try (var shapes = mockConstruction(PolygonShape.class)) {
            assertThat(
                physicsObjectFactory.fixture(
                    world,
                    BodyDef.BodyType.DynamicBody,
                    new WorldPosition(new Vector2(1, 2)),
                    new Rectangle(3, 4)
                )
            ).isSameAs(fixture);

            var shape = shapes.constructed().getFirst();
            verify(shape).setAsBox(3, 4);
            verify(shape).dispose();
            assertDefinitions(
                BodyDef.BodyType.DynamicBody,
                new Vector2(1, 2),
                shape,
                false,
                0
            );
        }
    }

    @Test
    void whenCircleFixture_thenGroupIndexApplied() {
        try (var shapes = mockConstruction(CircleShape.class)) {
            physicsObjectFactory.fixture(
                world,
                BodyDef.BodyType.DynamicBody,
                new WorldPosition(new Vector2()),
                new Circle(2),
                new CollisionGroupIndex(-1)
            );

            var shape = shapes.constructed().getFirst();
            verify(shape).setRadius(2);
            verify(shape).dispose();
            assertDefinitions(
                BodyDef.BodyType.DynamicBody,
                new Vector2(),
                shape,
                false,
                -1
            );
        }
    }

    @Test
    void whenSensorFixture_thenSensorFlagSet() {
        try (var shapes = mockConstruction(CircleShape.class)) {
            physicsObjectFactory.sensorFixture(
                world,
                BodyDef.BodyType.DynamicBody,
                new WorldPosition(new Vector2()),
                1.5f
            );

            var shape = shapes.constructed().getFirst();
            verify(shape).setRadius(1.5f);
            assertDefinitions(
                BodyDef.BodyType.DynamicBody,
                new Vector2(),
                shape,
                true,
                0
            );
        }
    }

    @Test
    void whenBoundariesFixture_thenOpenChainCreated() {
        when(configuration.worldDimensions()).thenReturn(new Configuration.Dimensions(10, 20));
        try (var shapes = mockConstruction(ChainShape.class)) {
            physicsObjectFactory.boundariesFixture(world);

            var shape = shapes.constructed().getFirst();
            var vertices = ArgumentCaptor.forClass(float[].class);
            verify(shape).createChain(vertices.capture());
            assertThat(vertices.getValue()).containsExactly(
                0, 0,
                0, 20,
                10, 20,
                10, 0
            );
            assertDefinitions(
                BodyDef.BodyType.StaticBody,
                new Vector2(),
                shape,
                false,
                0
            );
        }
    }

    @Test
    void whenWeldJoint_thenJointDefinitionCreated() {
        when(first.getPosition()).thenReturn(new Vector2(1, 2));
        when(first.getLocalPoint(any())).thenReturn(new Vector2());
        when(second.getLocalPoint(any())).thenReturn(new Vector2());
        when(world.createJoint(any(WeldJointDef.class))).thenReturn(weldJoint);

        assertThat(physicsObjectFactory.weldJoint(world, first, second))
            .isSameAs(weldJoint);

        var definition = ArgumentCaptor.forClass(WeldJointDef.class);
        verify(world).createJoint(definition.capture());
        assertThat(definition.getValue().bodyA).isSameAs(first);
        assertThat(definition.getValue().bodyB).isSameAs(second);
        assertThat(definition.getValue().frequencyHz).isZero();
        assertThat(definition.getValue().dampingRatio).isZero();
    }

    private void assertDefinitions(
        BodyDef.BodyType bodyType,
        Vector2 position,
        Shape shape,
        boolean sensor,
        int groupIndex
    ) {
        var bodyDefinition = ArgumentCaptor.forClass(BodyDef.class);
        verify(world).createBody(bodyDefinition.capture());
        assertThat(bodyDefinition.getValue().type).isEqualTo(bodyType);
        assertThat(bodyDefinition.getValue().position)
            .usingRecursiveComparison()
            .isEqualTo(position);
        assertThat(bodyDefinition.getValue().fixedRotation).isTrue();

        var fixtureDefinition = ArgumentCaptor.forClass(FixtureDef.class);
        verify(body).createFixture(fixtureDefinition.capture());
        assertThat(fixtureDefinition.getValue().shape).isSameAs(shape);
        assertThat(fixtureDefinition.getValue().density).isEqualTo(1);
        assertThat(fixtureDefinition.getValue().friction).isZero();
        assertThat(fixtureDefinition.getValue().restitution).isEqualTo(1);
        assertThat(fixtureDefinition.getValue().isSensor).isEqualTo(sensor);
        assertThat(fixtureDefinition.getValue().filter.groupIndex)
            .isEqualTo((short) groupIndex);
    }
}
