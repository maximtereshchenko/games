package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.World;
import com.github.maximtereshchenko.games.ecs.WorldEdit;

final class WallSpawningSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> wallPolicyEntities;

    WallSpawningSystem(World world) {
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
                    PaletteColor.WALL,
                    new Opacity(1)
                );
            }
        }
    }
}
