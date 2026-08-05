package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

final class WallClusterFoodSpawningSystem implements System {

    private final Iterable<Entity> turnStartedEntities;
    private final Iterable<Entity> initializingEntities;
    private final Iterable<Entity> wallClusterFoodPolicyEntities;
    private final Iterable<Entity> nonBackgroundEntities;
    private final Iterable<Entity> foodEntities;
    private final Iterable<Entity> worldDimensionsEntities;
    private final Random random;

    WallClusterFoodSpawningSystem(World world, Random random) {
        this.turnStartedEntities = world.entities(
            new Query().all(TurnStarted.class)
        );
        this.initializingEntities = world.entities(
            new Query().all(WallClusterFoodPolicy.class, Initializing.class)
        );
        this.wallClusterFoodPolicyEntities = world.entities(
            new Query().all(WallClusterFoodPolicy.class)
        );
        this.nonBackgroundEntities = world.entities(
            new Query()
                .all(WorldPosition.class)
                .none(Background.class)
        );
        this.foodEntities = world.entities(
            new Query().all(Food.class)
        );
        this.worldDimensionsEntities = world.entities(
            new Query().all(WorldDimensions.class)
        );
        this.random = random;
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        if ((initializingEntities.iterator().hasNext() || turnStartedEntities.iterator().hasNext()) && !foodEntities.iterator().hasNext()) {
            for (var _ : wallClusterFoodPolicyEntities) {
                for (var worldDimensionsEntity : worldDimensionsEntities) {
                    var worldDimensions = worldDimensionsEntity.component(WorldDimensions.class);
                    var worldPositions = worldPositions();
                    if (space(worldDimensions) - worldPositions.size() > 9) {
                        var spawnArea = new WorldPosition[3][3];
                        for (var row : spawnArea) {
                            for (var columnIndex = 0; columnIndex < row.length; columnIndex++) {
                                row[columnIndex] = new WorldPosition();
                            }
                        }
                        do {
                            var x = random.nextInt(1, worldDimensions.width() - 1);
                            var y = random.nextInt(1, worldDimensions.height() - 1);
                            var xOffset = -1;
                            var yOffset = -1;
                            for (var row : spawnArea) {
                                for (var worldPosition : row) {
                                    worldPosition.x = x + xOffset;
                                    worldPosition.y = y + yOffset;
                                    xOffset++;
                                }
                                xOffset = -1;
                                yOffset++;
                            }
                        } while (
                            Stream.of(spawnArea)
                                .flatMap(Stream::of)
                                .anyMatch(worldPositions::contains)
                        );
                        for (var rowIndex = 0; rowIndex < spawnArea.length; rowIndex++) {
                            var row = spawnArea[rowIndex];
                            for (var columnIndex = 0; columnIndex < row.length; columnIndex++) {
                                var worldPosition = row[columnIndex];
                                if (rowIndex == 1 && columnIndex == 1) {
                                    worldEdit.addComponents(
                                        worldEdit.createEntity(),
                                        Wall.INSTANCE,
                                        worldPosition,
                                        PaletteColor.WALL,
                                        new Opacity(1)
                                    );
                                } else {
                                    worldEdit.addComponents(
                                        worldEdit.createEntity(),
                                        new Food(1),
                                        worldPosition,
                                        PaletteColor.FOOD,
                                        new Opacity(1)
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int space(WorldDimensions worldDimensions) {
        return worldDimensions.height() * worldDimensions.width();
    }

    private Set<WorldPosition> worldPositions() {
        var worldPositions = new HashSet<WorldPosition>();
        for (var entity : nonBackgroundEntities) {
            worldPositions.add(entity.component(WorldPosition.class));
        }
        return worldPositions;
    }
}
