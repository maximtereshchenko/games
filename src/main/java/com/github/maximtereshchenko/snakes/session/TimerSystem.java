package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

final class TimerSystem extends TurnBasedSystem {

    private final Iterable<Entity> timerEntities;

    TimerSystem(World world) {
        super(world);
        this.timerEntities = world.entities(
            new Query().all(Timer.class)
        );
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        for (var entity : timerEntities) {
            var timer = entity.component(Timer.class);
            if (timer.turnsRemaining == 0) {
                timer.turnsRemaining = timer.period;
            }
            timer.turnsRemaining--;
        }
    }
}
