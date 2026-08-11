package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class WallSpawningSystem extends TurnBasedSystem {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> wallPolicyEntities;

    WallSpawningSystem(Registry registry) {
        super(registry);
        this.foodConsumedEntities = registry.entities(
            new Query().all(FoodConsumed.class, WorldPosition.class)
        );
        this.wallPolicyEntities = registry.entities(
            new Query().all(WallPolicy.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var _ : wallPolicyEntities) {
            for (var foodConsumedEntity : foodConsumedEntities) {
                var worldPosition = foodConsumedEntity.component(WorldPosition.class);
                var wallWorldPosition = new WorldPosition();
                wallWorldPosition.copy(worldPosition);
                registryEdit.addComponents(
                    registryEdit.createEntity(),
                    Wall.INSTANCE,
                    wallWorldPosition,
                    PaletteColor.WALL,
                    new Opacity(1)
                );
            }
        }
    }
}
