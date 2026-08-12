package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class PhysicsSystem implements System {

    private final World world;

    PhysicsSystem(World world) {
        this.world = world;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        world.step(deltaTimeSeconds, 8, 3);
        for (var entry : collisions().entrySet()) {
            registryEdit.addComponents(
                entry.getKey(),
                new Collisions(entry.getValue())
            );
        }
    }

    private Map<Integer, Set<Integer>> collisions() {
        var collisions = new HashMap<Integer, Set<Integer>>();
        for (var contact : world.getContactList()) {
            if (contact.isTouching()) {
                register(collisions, contact);
            }
        }
        return collisions;
    }

    private void register(
        Map<Integer, Set<Integer>> collisions,
        Contact contact
    ) {
        var firstUserData = contact.getFixtureA().getUserData();
        var secondUserData = contact.getFixtureB().getUserData();
        if (firstUserData == null || secondUserData == null) {
            return;
        }
        var firstEntityId = (int) firstUserData;
        var secondEntityId = (int) secondUserData;
        register(collisions, firstEntityId, secondEntityId);
        register(collisions, secondEntityId, firstEntityId);
    }

    private void register(
        Map<Integer, Set<Integer>> collisions,
        int first,
        int second
    ) {
        collisions.computeIfAbsent(first, _ -> new HashSet<>())
            .add(second);
    }
}
