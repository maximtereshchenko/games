package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class WarpedRelativeDirectionSystem extends TurnBasedSystem {

    private final Iterable<Entity> warpedEntities;

    WarpedRelativeDirectionSystem(World world) {
        super(world);
        this.warpedEntities = world.entities(
            new Query()
                .all(
                    Warped.class,
                    WarpedRelativeDirection.class,
                    Direction.class,
                    DirectionIntent.class
                )
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var warpedEntity : warpedEntities) {
            var direction = warpedEntity.component(Direction.class)
                .relative(
                    warpedEntity.component(WarpedRelativeDirection.class)
                        .value()
                );
            worldEdit.addComponents(warpedEntity.id(), direction);
            warpedEntity.component(DirectionIntent.class).value = direction;
        }
    }
}
