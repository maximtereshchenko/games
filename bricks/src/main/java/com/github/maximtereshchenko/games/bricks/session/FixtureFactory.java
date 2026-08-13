package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public final class FixtureFactory {

    Fixture fixture(
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
