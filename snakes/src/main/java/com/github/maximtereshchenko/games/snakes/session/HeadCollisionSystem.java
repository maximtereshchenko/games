package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class HeadCollisionSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;
    private final Iterable<Entity> obstacleEntities;

    HeadCollisionSystem(Registry registry) {
        super(registry);
        this.headEntities = registry.entities(
            new Query().all(Head.class, WorldPositionIntent.class)
        );
        this.obstacleEntities = registry.entities(
            new Query()
                .all(WorldPosition.class)
                .one(Segment.class, Wall.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var headEntity : headEntities) {
            var headWorldPositionIntent = headEntity.component(WorldPositionIntent.class).value();
            for (var obstacleEntity : obstacleEntities) {
                var obstacleWorldPosition = obstacleEntity.component(WorldPosition.class);
                if (headWorldPositionIntent.equals(obstacleWorldPosition)) {
                    registryEdit.addComponents(headEntity.id(), Dead.INSTANCE);
                }
            }
        }
    }
}
