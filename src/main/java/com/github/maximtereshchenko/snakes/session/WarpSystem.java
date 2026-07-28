package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class WarpSystem extends TurnBasedSystem {

    private final Iterable<Entity> warpEntities;
    private final Iterable<Entity> worldPositionIntentEntities;

    WarpSystem(World world) {
        super(world);
        this.warpEntities = world.entities(
            new Query().all(Warp.class, WorldPosition.class)
        );
        this.worldPositionIntentEntities = world.entities(
            new Query().all(WorldPositionIntent.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var warpEntity : warpEntities) {
            for (var worldPositionIntentEntity : worldPositionIntentEntities) {
                var worldPositionIntent = worldPositionIntentEntity.component(WorldPositionIntent.class);
                if (worldPositionIntent.value.equals(warpEntity.component(WorldPosition.class))) {
                    worldPositionIntent.value.copy(warpEntity.component(Warp.class).worldPosition());
                    worldEdit.addComponents(worldPositionIntentEntity.id(), Warped.INSTANCE);
                }
            }
        }
    }
}
