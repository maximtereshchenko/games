package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class WarpedRelativeDirectionSystem extends TurnBasedSystem {

    private final Iterable<Entity> warpedEntities;

    WarpedRelativeDirectionSystem(Registry registry) {
        super(registry);
        this.warpedEntities = registry.view(
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
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var warpedEntity : warpedEntities) {
            var direction = warpedEntity.component(Direction.class)
                .relative(
                    warpedEntity.component(WarpedRelativeDirection.class)
                        .value()
                );
            registryEdit.addComponents(warpedEntity.id(), direction);
            warpedEntity.component(DirectionIntent.class).value = direction;
        }
    }
}
