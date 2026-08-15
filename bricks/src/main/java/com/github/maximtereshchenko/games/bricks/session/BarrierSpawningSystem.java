package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

final class BarrierSpawningSystem implements System {

    private final Iterable<Entity> spawnBarrierEntities;
    private final Iterable<Entity> barrierEntities;
    private final Viewport viewport;

    BarrierSpawningSystem(Registry registry, Viewport viewport) {
        this.spawnBarrierEntities = registry.entities(
            new Query().all(SpawnBarrier.class, Activated.class)
        );
        this.barrierEntities = registry.entities(
            new Query().all(Barrier.class)
        );
        this.viewport = viewport;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (
            !spawnBarrierEntities.iterator().hasNext() ||
            barrierEntities.iterator().hasNext()
        ) {
            return;
        }
        var rectangle = new Rectangle(viewport.getWorldWidth() / 2, 0.1f);
        registryEdit.addComponents(
            registryEdit.createEntity(),
            new Barrier(0),
            BodyDef.BodyType.StaticBody,
            rectangle,
            new WorldPosition(
                new Vector2(
                    rectangle.halfWidth,
                    rectangle.halfHeight
                )
            ),
            new Visible(Color.valueOf("#ff9859"))
        );
    }
}
