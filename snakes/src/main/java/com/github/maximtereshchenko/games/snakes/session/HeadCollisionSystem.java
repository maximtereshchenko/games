package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import com.github.maximtereshchenko.games.ecs.WorldEdit;

final class HeadCollisionSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;
    private final Iterable<Entity> obstacleEntities;

    HeadCollisionSystem(World world) {
        super(world);
        this.headEntities = world.entities(
            new Query().all(Head.class, WorldPositionIntent.class)
        );
        this.obstacleEntities = world.entities(
            new Query()
                .all(WorldPosition.class)
                .one(Segment.class, Wall.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var headEntity : headEntities) {
            var headWorldPositionIntent = headEntity.component(WorldPositionIntent.class).value();
            for (var obstacleEntity : obstacleEntities) {
                var obstacleWorldPosition = obstacleEntity.component(WorldPosition.class);
                if (headWorldPositionIntent.equals(obstacleWorldPosition)) {
                    worldEdit.addComponents(headEntity.id(), Dead.INSTANCE);
                }
            }
        }
    }
}
