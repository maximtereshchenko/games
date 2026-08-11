package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;
import com.github.maximtereshchenko.games.ecs.System;

final class PhysicsSystem implements System {

    private final World physicsWorld;

    PhysicsSystem(World physicsWorld) {
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        physicsWorld.step(deltaTimeSeconds, 16, 6);
        for (var contact : physicsWorld.getContactList()) {
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
