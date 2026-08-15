package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;

final class SensorFixtureSystem extends FixtureSystem {

    private final World world;
    private final PhysicsObjectFactory physicsObjectFactory;

    SensorFixtureSystem(
        Registry registry,
        World world,
        PhysicsObjectFactory physicsObjectFactory
    ) {
        super(
            registry,
            new Query()
                .all(Sensor.class)
                .one(Circle.class, Star.class)
        );
        this.world = world;
        this.physicsObjectFactory = physicsObjectFactory;
    }

    @Override
    Fixture fixture(
        Entity entity,
        BodyDef.BodyType bodyType,
        WorldPosition worldPosition
    ) {
        return physicsObjectFactory.sensorFixture(
            world,
            bodyType,
            worldPosition,
            radius(entity)
        );
    }

    private float radius(Entity entity) {
        var circle = entity.component(Circle.class);
        if (circle != null) {
            return circle.radius();
        }
        var star = entity.component(Star.class);
        return star.radius();
    }
}
