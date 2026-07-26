package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class HeadCollisionTargetSystem extends TurnBasedSystem {

    private final Iterable<Entity> headEntities;
    private final Iterable<Entity> nonHeadEntities;

    HeadCollisionTargetSystem(World world) {
        super(world);
        this.headEntities = world.entities(
            new Query().all(Head.class, Position.class)
        );
        this.nonHeadEntities = world.entities(
            new Query().all(Position.class).none(Head.class, HeadCollisionTarget.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var head : headEntities) {
            for (var target : nonHeadEntities) {
                if (head.component(Position.class).equals(target.component(Position.class))) {
                    worldEdit.addComponents(
                        target.id(),
                        HeadCollisionTarget.INSTANCE
                    );
                }
            }
        }
    }
}
