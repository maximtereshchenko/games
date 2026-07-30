package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class WallPolicySystem extends TurnBasedSystem {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> wallPolicyEntities;

    WallPolicySystem(World world) {
        super(world);
        this.foodConsumedEntities = world.entities(
            new Query().all(FoodConsumed.class, WorldPosition.class)
        );
        this.wallPolicyEntities = world.entities(
            new Query().all(WallPolicy.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var _ : wallPolicyEntities) {
            for (var foodConsumedEntity : foodConsumedEntities) {
                var worldPosition = foodConsumedEntity.component(WorldPosition.class);
                var wallWorldPosition = new WorldPosition();
                wallWorldPosition.copy(worldPosition);
                worldEdit.addComponents(
                    worldEdit.createEntity(),
                    Wall.INSTANCE,
                    wallWorldPosition,
                    Colored.WALL
                );
            }
        }
    }
}
