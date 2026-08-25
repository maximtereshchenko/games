package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.physics.box2d.*;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class PhysicsSystem implements System, ContactListener {

    private final Iterable<Entity> entities;
    private final World world;
    private final Map<Integer, Set<Integer>> collisions;

    PhysicsSystem(Registry registry, World world) {
        this.entities = registry.view(
            new Query().all(PhysicsPolicy.class)
        );
        this.world = world;
        this.collisions = new HashMap<>();
        world.setContactListener(this);
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var physicsPolicy = entity.component(PhysicsPolicy.class);
            physicsPolicy.accumulatedTimeSeconds += Math.min(
                deltaTimeSeconds,
                physicsPolicy.maxFrameTimeSeconds
            );
            while (physicsPolicy.accumulatedTimeSeconds >= physicsPolicy.stepTimeSeconds) {
                physicsPolicy.accumulatedTimeSeconds -= physicsPolicy.stepTimeSeconds;
                world.step(physicsPolicy.stepTimeSeconds, 8, 3);
            }
        }
        for (var entry : collisions.entrySet()) {
            registryEdit.addComponents(
                entry.getKey(),
                new Collisions(entry.getValue())
            );
        }
        collisions.clear();
    }

    @Override
    public void beginContact(Contact contact) {
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

    @Override
    public void endContact(Contact contact) {
        //empty
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //empty
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //empty
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
