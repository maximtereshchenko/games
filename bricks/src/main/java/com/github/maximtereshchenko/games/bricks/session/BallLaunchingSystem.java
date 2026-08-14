package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BallLaunchingSystem implements System {

    private final Iterable<Entity> entities;
    private final World world;

    BallLaunchingSystem(Registry registry, World world) {
        this.entities = registry.entities(
            new Query()
                .all(
                    WeldJoint.class,
                    Speed.class,
                    Velocity.class
                )
        );
        this.world = world;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (!Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            return;
        }
        for (var entity : entities) {
            var joint = entity.component(WeldJoint.class);
            var speed = entity.component(Speed.class);
            var velocity = entity.component(Velocity.class);
            world.destroyJoint(joint);
            registryEdit.removeComponents(entity.id(), WeldJoint.class);
            velocity.vector2().y = speed.value();
        }
    }
}
