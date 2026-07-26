package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.Entity;
import com.github.maximtereshchenko.ecs.Query;
import com.github.maximtereshchenko.ecs.World;
import com.github.maximtereshchenko.ecs.WorldEdit;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

final class FoodSpawningSystem extends TurnBasedSystem {

    private final Iterable<Entity> nonBackgroundPositionEntities;
    private final Iterable<Entity> worldDimensionsEntities;
    private final Iterable<Entity> foodEntities;
    private final EntityFactory entityFactory;
    private final Random random;
    private final int maxFood;

    FoodSpawningSystem(
        World world, EntityFactory entityFactory,
        Random random,
        int maxFood
    ) {
        super(world);
        this.nonBackgroundPositionEntities = world.entities(
            new Query()
                .all(Position.class)
                .none(Background.class)
        );
        this.worldDimensionsEntities = world.entities(
            new Query().all(WorldDimensions.class)
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class)
        );
        this.entityFactory = entityFactory;
        this.random = random;
        this.maxFood = maxFood;
    }

    @Override
    void onTurnStarted(WorldEdit worldEdit) {
        var positions = positions();
        for (var entity : worldDimensionsEntities) {
            var worldDimensions = entity.component(WorldDimensions.class);
            for (var i = currentFood(); positions.size() < space(worldDimensions) && i < maxFood; i++) {
                var position = position(positions, worldDimensions);
                positions.add(position);
                entityFactory.createFood(worldEdit, position);
            }
        }
    }

    private Set<Position> positions() {
        var positions = new HashSet<Position>();
        for (var entity : nonBackgroundPositionEntities) {
            positions.add(entity.component(Position.class));
        }
        return positions;
    }

    private int space(WorldDimensions worldDimensions) {
        return worldDimensions.height() * worldDimensions.width();
    }

    private Position position(Set<Position> positions, WorldDimensions worldDimensions) {
        while (true) {
            var position = new Position(
                random.nextInt(worldDimensions.width()),
                random.nextInt(worldDimensions.height())
            );
            if (!positions.contains(position)) {
                return position;
            }
        }
    }

    private int currentFood() {
        var count = 0;
        for (var _ : foodEntities) {
            count++;
        }
        return count;
    }
}
