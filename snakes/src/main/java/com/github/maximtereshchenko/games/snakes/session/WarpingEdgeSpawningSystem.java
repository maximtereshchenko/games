package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.function.Predicate;

final class WarpingEdgeSpawningSystem implements System {

    private final Iterable<Entity> foodConsumedEntities;
    private final Iterable<Entity> initializingEntities;
    private final Iterable<Entity> worldDimensionsEntities;
    private final Iterable<Entity> warpingPolicyEntities;

    WarpingEdgeSpawningSystem(Registry registry) {
        this.foodConsumedEntities = registry.entities(
            new Query().all(FoodConsumed.class)
        );
        this.initializingEntities = registry.entities(
            new Query().all(WarpingPolicy.class, Initializing.class)
        );
        this.worldDimensionsEntities = registry.entities(
            new Query().all(WorldDimensions.class)
        );
        this.warpingPolicyEntities = registry.entities(
            new Query().all(WarpingPolicy.class)
        );
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        spawnWarpingEdges(registryEdit, initializingEntities, _ -> true);
        if (!foodConsumedEntities.iterator().hasNext()) {
            return;
        }
        spawnWarpingEdges(
            registryEdit,
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
        RegistryEdit registryEdit,
        Iterable<Entity> entities,
        Predicate<WarpingPolicy> predicate
    ) {
        for (var warpingPolicyEntity : entities) {
            var warpingPolicy = warpingPolicyEntity.component(WarpingPolicy.class);
            if (predicate.test(warpingPolicy)) {
                spawnWarpingEdges(registryEdit, warpingPolicy.layers);
                warpingPolicy.layers++;
            }
        }
    }

    private void spawnWarpingEdges(
        RegistryEdit registryEdit,
        int warpingLayers
    ) {
        for (var worldDimensionsEntity : worldDimensionsEntities) {
            var worldDimensions = worldDimensionsEntity.component(WorldDimensions.class);
            var rightBoundary = worldDimensions.width() - warpingLayers;
            var topBoundary = worldDimensions.height() - warpingLayers - 1;
            for (var i = warpingLayers; i < rightBoundary; i++) {
                createWarp(registryEdit, WarpingEdge.TOP, i, topBoundary);
            }
            for (var i = warpingLayers; i < rightBoundary; i++) {
                createWarp(registryEdit, WarpingEdge.BOTTOM, i, warpingLayers);
            }
            for (var i = warpingLayers + 1; i < topBoundary; i++) {
                createWarp(registryEdit, WarpingEdge.LEFT, warpingLayers, i);
            }
            for (var i = warpingLayers + 1; i < topBoundary; i++) {
                createWarp(
                    registryEdit,
                    WarpingEdge.RIGHT,
                    worldDimensions.width() - warpingLayers - 1,
                    i
                );
            }
        }
    }

    private void createWarp(RegistryEdit registryEdit, WarpingEdge warpingEdge, int x, int y) {
        registryEdit.addComponents(
            registryEdit.createEntity(),
            warpingEdge,
            new WorldPosition(x, y),
            PaletteColor.WARP,
            new Opacity(1)
        );
    }
}
