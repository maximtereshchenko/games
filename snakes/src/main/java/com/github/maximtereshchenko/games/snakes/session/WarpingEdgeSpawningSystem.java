package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.function.Predicate;

final class WarpingEdgeSpawningSystem implements System {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> initializingEntities;
    private final Iterable<Entity> worldDimensionsEntities;
    private final Iterable<Entity> warpingPolicyEntities;

    WarpingEdgeSpawningSystem(World world) {
        this.foodConsumedEntities = world.entities(
            new Query().all(FoodConsumed.class)
        );
        this.initializingEntities = world.entities(
            new Query().all(WarpingPolicy.class, Initializing.class)
        );
        this.worldDimensionsEntities = world.entities(
            new Query().all(WorldDimensions.class)
        );
        this.warpingPolicyEntities = world.entities(
            new Query().all(WarpingPolicy.class)
        );
    }

    @Override
    public void update(WorldEdit worldEdit, float deltaTimeSeconds) {
        spawnWarpingEdges(worldEdit, initializingEntities, _ -> true);
        if (!foodConsumedEntities.iterator().hasNext()) {
            return;
        }
        spawnWarpingEdges(
            worldEdit,
            warpingPolicyEntities,
            this::decrement
        );
    }

    private boolean decrement(WarpingPolicy warpingPolicy) {
        warpingPolicy.remainingConsumedFood--;
        if (warpingPolicy.remainingConsumedFood != 0) {
            return false;
        }
        warpingPolicy.remainingConsumedFood = warpingPolicy.periodConsumedFood;
        return true;
    }

    private void spawnWarpingEdges(
        WorldEdit worldEdit,
        Iterable<Entity> entities,
        Predicate<WarpingPolicy> predicate
    ) {
        for (var warpingPolicyEntity : entities) {
            var warpingPolicy = warpingPolicyEntity.component(WarpingPolicy.class);
            if (predicate.test(warpingPolicy)) {
                spawnWarpingEdges(worldEdit, warpingPolicy.layers);
                warpingPolicy.layers++;
            }
        }
    }

    private void spawnWarpingEdges(
        WorldEdit worldEdit,
        int warpingLayers
    ) {
        for (var worldDimensionsEntity : worldDimensionsEntities) {
            var worldDimensions = worldDimensionsEntity.component(WorldDimensions.class);
            var rightBoundary = worldDimensions.width() - warpingLayers;
            var topBoundary = worldDimensions.height() - warpingLayers - 1;
            for (var i = warpingLayers; i < rightBoundary; i++) {
                createWarp(worldEdit, WarpingEdge.TOP, i, topBoundary);
            }
            for (var i = warpingLayers; i < rightBoundary; i++) {
                createWarp(worldEdit, WarpingEdge.BOTTOM, i, warpingLayers);
            }
            for (var i = warpingLayers + 1; i < topBoundary; i++) {
                createWarp(worldEdit, WarpingEdge.LEFT, warpingLayers, i);
            }
            for (var i = warpingLayers + 1; i < topBoundary; i++) {
                createWarp(
                    worldEdit,
                    WarpingEdge.RIGHT,
                    worldDimensions.width() - warpingLayers - 1,
                    i
                );
            }
        }
    }

    private void createWarp(WorldEdit worldEdit, WarpingEdge warpingEdge, int x, int y) {
        worldEdit.addComponents(
            worldEdit.createEntity(),
            warpingEdge,
            new WorldPosition(x, y),
            PaletteColor.WARP,
            new Opacity(1)
        );
    }
}
