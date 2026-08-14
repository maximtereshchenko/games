package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Registry;

final class SensorFixtureSystem extends FixtureSystem {

    private final World world;
    private final PhysicsObjectFactory physicsObjectFactory;

    SensorFixtureSystem(
        Registry registry,
        World world,
        PhysicsObjectFactory physicsObjectFactory
    ) {
        super(registry, Circle.class, Sensor.class);
        this.world = world;
        this.physicsObjectFactory = physicsObjectFactory;
    }

    @Override
    Fixture fixture(
        Entity entity,
        BodyDef.BodyType bodyType,
        WorldPosition worldPosition
    ) {
        var circle = entity.component(Circle.class);
        return physicsObjectFactory.sensorFixture(
            world,
            bodyType,
            worldPosition,
            circle
        );
    }
}
