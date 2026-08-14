package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class PhysicsObjectFactory {

    Joint weldJoint(
        World world,
        Body first,
        Body second
    ) {
        var weldJointDefinition = new WeldJointDef();
        weldJointDefinition.initialize(
            first,
            second,
            first.getPosition()
        );
        weldJointDefinition.frequencyHz = 0;
        weldJointDefinition.dampingRatio = 0;
        return world.createJoint(weldJointDefinition);
    }

    Fixture fixture(
        World world,
        BodyDef.BodyType bodyType,
        WorldPosition worldPosition,
        Rectangle rectangle
    ) {
        var shape = shape(rectangle);
        var fixture = fixture(
            world,
            bodyType,
            worldPosition.vector2(),
            shape,
            false,
            0
        );
        shape.dispose();
        return fixture;
    }

    Fixture sensorFixture(
        World world,
        BodyDef.BodyType bodyType,
        WorldPosition worldPosition,
        Circle circle
    ) {
        var shape = shape(circle);
        var fixture = fixture(
            world,
            bodyType,
            worldPosition.vector2(),
            shape,
            true,
            0
        );
        shape.dispose();
        return fixture;
    }

    Fixture fixture(
        World world,
        BodyDef.BodyType bodyType,
        WorldPosition worldPosition,
        Circle circle,
        CollisionGroupIndex collisionGroupIndex
    ) {
        var shape = shape(circle);
        var fixture = fixture(
            world,
            bodyType,
            worldPosition.vector2(),
            shape,
            false,
            collisionGroupIndex.value()
        );
        shape.dispose();
        return fixture;
    }

    void createBoundaries(World world, Viewport viewport) {
        var shape = new ChainShape();
        shape.createChain(
            new float[]{
                0, 0,
                0, viewport.getWorldHeight(),
                viewport.getWorldWidth(), viewport.getWorldHeight(),
                viewport.getWorldWidth(), 0
            }
        );
        fixture(
            world,
            BodyDef.BodyType.StaticBody,
            new Vector2(),
            shape,
            false,
            0
        );
        shape.dispose();
    }

    private Shape shape(Rectangle rectangle) {
        var shape = new PolygonShape();
        shape.setAsBox(rectangle.halfWidth, rectangle.halfHeight);
        return shape;
    }

    private Shape shape(Circle circle) {
        var shape = new CircleShape();
        shape.setRadius(circle.radius());
        return shape;
    }

    private Fixture fixture(
        World world,
        BodyDef.BodyType bodyType,
        Vector2 vector2,
        Shape shape,
        boolean isSensor,
        int collisionGroupIndex
    ) {
        return world.createBody(
                bodyDefinition(bodyType, vector2)
            )
            .createFixture(
                fixtureDefinition(
                    shape,
                    isSensor,
                    collisionGroupIndex
                )
            );
    }

    private FixtureDef fixtureDefinition(
        Shape shape,
        boolean isSensor,
        int collisionGroupIndex
    ) {
        var fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = shape;
        fixtureDefinition.density = 1;
        fixtureDefinition.friction = 0;
        fixtureDefinition.restitution = 1;
        fixtureDefinition.isSensor = isSensor;
        fixtureDefinition.filter.groupIndex =
            (short) collisionGroupIndex;
        return fixtureDefinition;
    }

    private BodyDef bodyDefinition(
        BodyDef.BodyType bodyType,
        Vector2 vector2
    ) {
        var bodyDefinition = new BodyDef();
        bodyDefinition.type = bodyType;
        bodyDefinition.position.set(vector2);
        bodyDefinition.fixedRotation = true;
        return bodyDefinition;
    }
}
