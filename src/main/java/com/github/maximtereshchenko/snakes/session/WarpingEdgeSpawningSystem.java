package com.github.maximtereshchenko.snakes.session;

import com.github.maximtereshchenko.ecs.*;
import com.github.maximtereshchenko.ecs.System;

import java.util.function.Predicate;

//TODO refactoring
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
        spawnWarpingEdges(worldEdit, initializingEntities, warpingPolicy -> true);
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
            for (var i = warpingLayers; i < worldDimensions.width() - warpingLayers; i++) {
                worldEdit.addComponents(
                    worldEdit.createEntity(),
                    WarpingEdge.TOP,
                    new WorldPosition(i, worldDimensions.height() - warpingLayers - 1),
                    PaletteColor.WARP,
                    new Opacity(1)
                );
            }
            for (var i = warpingLayers; i < worldDimensions.width() - warpingLayers; i++) {
                worldEdit.addComponents(
                    worldEdit.createEntity(),
                    WarpingEdge.BOTTOM,
                    new WorldPosition(i, warpingLayers),
                    PaletteColor.WARP,
                    new Opacity(1)
                );
            }
            for (var i = warpingLayers + 1; i < worldDimensions.height() - warpingLayers - 1; i++) {
                worldEdit.addComponents(
                    worldEdit.createEntity(),
                    WarpingEdge.LEFT,
                    new WorldPosition(warpingLayers, i),
                    PaletteColor.WARP,
                    new Opacity(1)
                );
            }
            for (var i = warpingLayers + 1; i < worldDimensions.height() - warpingLayers - 1; i++) {
                worldEdit.addComponents(
                    worldEdit.createEntity(),
                    WarpingEdge.RIGHT,
                    new WorldPosition(worldDimensions.width() - warpingLayers - 1, i),
                    PaletteColor.WARP,
                    new Opacity(1)
                );
            }
        }
    }
}
