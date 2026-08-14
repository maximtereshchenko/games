package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class PaddleJointSystem implements System {

    private final Iterable<Entity> attachingEntities;
    private final Iterable<Entity> paddleEntities;
    private final World world;
    private final PhysicsObjectFactory physicsObjectFactory;

    PaddleJointSystem(
        Registry registry,
        World world,
        PhysicsObjectFactory physicsObjectFactory
    ) {
        this.attachingEntities = registry.entities(
            new Query().all(Attaching.class, Fixture.class)
        );
        this.paddleEntities = registry.entities(
            new Query().all(Paddle.class, Fixture.class)
        );
        this.world = world;
        this.physicsObjectFactory = physicsObjectFactory;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var attachingEntity : attachingEntities) {
            var attachingFixture = attachingEntity.component(Fixture.class);
            for (var paddleEntity : paddleEntities) {
                var paddleFixture = paddleEntity.component(Fixture.class);
                registryEdit.addComponents(
                    attachingEntity.id(),
                    physicsObjectFactory.joint(
                        world,
                        attachingFixture.getBody(),
                        paddleFixture.getBody()
                    )
                );
            }
        }
    }
}
