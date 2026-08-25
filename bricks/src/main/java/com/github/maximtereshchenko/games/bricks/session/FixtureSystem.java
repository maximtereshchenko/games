package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

abstract class FixtureSystem implements System {

    private final Iterable<Entity> entities;

    FixtureSystem(
        Registry registry,
        Query query
    ) {
        this.entities = registry.view(
            query.all(BodyDef.BodyType.class, WorldPosition.class)
        );
    }

    @Override
    public final void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var bodyType = entity.component(BodyDef.BodyType.class);
            var worldPosition = entity.component(WorldPosition.class);
            var id = entity.id();
            var fixture = fixture(
                entity,
                bodyType,
                worldPosition
            );
            fixture.setUserData(id);
            registryEdit.addComponents(id, fixture);
        }
    }

    abstract Fixture fixture(
        Entity entity,
        BodyDef.BodyType bodyType,
        WorldPosition worldPosition
    );
}
