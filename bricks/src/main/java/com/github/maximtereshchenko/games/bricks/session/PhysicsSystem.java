package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;
import com.github.maximtereshchenko.games.ecs.System;

final class PhysicsSystem implements System {

    private final World world;

    PhysicsSystem(World world) {
        this.world = world;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        world.step(deltaTimeSeconds, 8, 3);
        for (var contact : world.getContactList()) {
            if (contact.isTouching()) {
                var firstUserData = contact.getFixtureA().getUserData();
                var secondUserData = contact.getFixtureB().getUserData();
                if (firstUserData != null && secondUserData != null) {
                    var firstEntityId = (int) firstUserData;
                    var secondEntityId = (int) secondUserData;
                    registryEdit.addComponents(firstEntityId, new Collision(secondEntityId));
                    registryEdit.addComponents(secondEntityId, new Collision(firstEntityId));
                }
            }
        }
    }
}
