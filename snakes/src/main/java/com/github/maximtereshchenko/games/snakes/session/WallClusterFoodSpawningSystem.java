package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

final class WallClusterFoodSpawningSystem implements System {

    private static final int CLUSTER_SIZE = 3;
    private static final int CLUSTER_CELLS = CLUSTER_SIZE * CLUSTER_SIZE;
    private static final int CLUSTER_CENTER = CLUSTER_SIZE / 2;

    private final Iterable<Entity> turnStartedEntities;
    private final Iterable<Entity> initializingEntities;
    private final Iterable<Entity> wallClusterFoodPolicyEntities;
    private final Iterable<Entity> nonBackgroundEntities;
    private final Iterable<Entity> foodEntities;
    private final Iterable<Entity> worldDimensionsEntities;
    private final Random random;

    WallClusterFoodSpawningSystem(Registry registry, Random random) {
        this.turnStartedEntities = registry.entities(
            new Query().all(TurnStarted.class)
        );
        this.initializingEntities = registry.entities(
            new Query().all(WallClusterFoodPolicy.class, Initializing.class)
        );
        this.wallClusterFoodPolicyEntities = registry.entities(
            new Query().all(WallClusterFoodPolicy.class)
        );
        this.nonBackgroundEntities = registry.entities(
            new Query()
                .all(WorldPosition.class)
                .none(Background.class)
        );
        this.foodEntities = registry.entities(
            new Query().all(Food.class)
        );
        this.worldDimensionsEntities = registry.entities(
            new Query().all(WorldDimensions.class)
        );
        this.random = random;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        if (foodEntities.iterator().hasNext()) {
            return;
        }
        if (!initializingEntities.iterator().hasNext() && !turnStartedEntities.iterator().hasNext()) {
            return;
        }
        spawnClusters(registryEdit);
    }

    private void spawnClusters(RegistryEdit registryEdit) {
        for (var _ : wallClusterFoodPolicyEntities) {
            for (var worldDimensionsEntity : worldDimensionsEntities) {
                spawnCluster(
                    registryEdit,
                    worldDimensionsEntity.component(WorldDimensions.class)
                );
            }
        }
    }

    private void spawnCluster(RegistryEdit registryEdit, WorldDimensions worldDimensions) {
        var worldPositions = worldPositions();
        if (space(worldDimensions) - worldPositions.size() <= CLUSTER_CELLS) {
            return;
        }
        spawnCluster(registryEdit, freeSpawnArea(worldDimensions, worldPositions));
    }

    private WorldPosition[][] freeSpawnArea(
        WorldDimensions worldDimensions,
        Set<WorldPosition> worldPositions
    ) {
        var spawnArea = newSpawnArea();
        do {
            placeSpawnArea(spawnArea, worldDimensions);
        } while (isOccupied(spawnArea, worldPositions));
        return spawnArea;
    }

    private WorldPosition[][] newSpawnArea() {
        var spawnArea = new WorldPosition[CLUSTER_SIZE][CLUSTER_SIZE];
        for (var row : spawnArea) {
            for (var columnIndex = 0; columnIndex < row.length; columnIndex++) {
                row[columnIndex] = new WorldPosition();
            }
        }
        return spawnArea;
    }

    private void placeSpawnArea(WorldPosition[][] spawnArea, WorldDimensions worldDimensions) {
        var x = random.nextInt(1, worldDimensions.width() - 1);
        var y = random.nextInt(1, worldDimensions.height() - 1);
        for (var rowIndex = 0; rowIndex < spawnArea.length; rowIndex++) {
            var row = spawnArea[rowIndex];
            for (var columnIndex = 0; columnIndex < row.length; columnIndex++) {
                var worldPosition = row[columnIndex];
                worldPosition.x = x + columnIndex - CLUSTER_CENTER;
                worldPosition.y = y + rowIndex - CLUSTER_CENTER;
            }
        }
    }

    private boolean isOccupied(WorldPosition[][] spawnArea, Set<WorldPosition> worldPositions) {
        for (var row : spawnArea) {
            for (var worldPosition : row) {
                if (worldPositions.contains(worldPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void spawnCluster(RegistryEdit registryEdit, WorldPosition[][] spawnArea) {
        for (var rowIndex = 0; rowIndex < spawnArea.length; rowIndex++) {
            var row = spawnArea[rowIndex];
            for (var columnIndex = 0; columnIndex < row.length; columnIndex++) {
                var worldPosition = row[columnIndex];
                if (rowIndex == CLUSTER_CENTER && columnIndex == CLUSTER_CENTER) {
                    registryEdit.addComponents(
                        registryEdit.createEntity(),
                        Wall.INSTANCE,
                        worldPosition,
                        PaletteColor.WALL,
                        new Opacity(1)
                    );
                } else {
                    registryEdit.addComponents(
                        registryEdit.createEntity(),
                        new Food(1),
                        worldPosition,
                        PaletteColor.FOOD,
                        new Opacity(1)
                    );
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
