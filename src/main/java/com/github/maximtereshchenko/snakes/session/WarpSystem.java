package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class WarpSystem extends TurnBasedSystem {

    private final Iterable<Entity> warpEntities;
    private final Iterable<Entity> headEntities;

    WarpSystem(World world) {
        super(world);
        this.warpEntities = world.entities(
            new Query().all(Warp.class, HeadCollisionTarget.class)
        );
        this.headEntities = world.entities(
            new Query()
                .all(
                    Head.class,
                    Position.class,
                    CurrentForwardDirection.class,
                    NextForwardDirection.class
                )
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var warpEntity : warpEntities) {
            for (var head : headEntities) {
                var warp = warpEntity.component(Warp.class);
                worldEdit.addComponents(head.id(), new Position(warp.position()));
                var currentForwardDirection = head.component(CurrentForwardDirection.class);
                currentForwardDirection.value = currentForwardDirection.value
                    .relative(warp.relativeDirection());
                head.component(NextForwardDirection.class).value =
                    currentForwardDirection.value;
            }
        }
    }
}
