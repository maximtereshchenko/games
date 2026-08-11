package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.ecs.Entity;
import com.github.maximtereshchenko.games.ecs.Query;
import com.github.maximtereshchenko.games.ecs.Registry;
import com.github.maximtereshchenko.games.ecs.RegistryEdit;

final class WarpingSystem extends TurnBasedSystem {

    private final Iterable<Entity> warpingEdgeEntities;
    private final Iterable<Entity> warpingDestinationEntities;
    private final Iterable<Entity> worldDimensionsEntities;
    private final Iterable<Entity> warpingPolicyEntities;

    WarpingSystem(Registry registry) {
        super(registry);
        this.warpingEdgeEntities = registry.entities(
            new Query().all(WarpingEdge.class, WorldPosition.class)
        );
        this.warpingDestinationEntities = registry.entities(
            new Query().all(WarpingDestinationEdge.class, WorldPositionIntent.class)
        );
        this.worldDimensionsEntities = registry.entities(
            new Query().all(WorldDimensions.class)
        );
        this.warpingPolicyEntities = registry.entities(
            new Query().all(WarpingPolicy.class)
        );
    }

    @Override
    void onTurnStarted(RegistryEdit registryEdit) {
        for (var warpingDestinationEntity : warpingDestinationEntities) {
            var worldPosition = warpingDestinationEntity.component(WorldPositionIntent.class).value();
            for (var warpingEdgeEntity : warpingEdgeEntities) {
                var warpWorldPosition = warpingEdgeEntity.component(WorldPosition.class);
                if (worldPosition.equals(warpWorldPosition)) {
                    warp(
                        worldPosition,
                        warpingDestinationEntity.component(WarpingDestinationEdge.class),
                        warpingEdgeEntity.component(WarpingEdge.class)
                    );
                    registryEdit.addComponents(warpingDestinationEntity.id(), Warped.INSTANCE);
                }
            }
        }
    }

    private void warp(
        WorldPosition worldPosition,
        WarpingDestinationEdge warpingDestinationEdge,
        WarpingEdge warpingEdge
    ) {
        for (var worldDimensionsEntity : worldDimensionsEntities) {
            var worldDimensions = worldDimensionsEntity.component(WorldDimensions.class);
            for (var warpingPolicyEntity : warpingPolicyEntities) {
                var warpLayers = warpingPolicyEntity.component(WarpingPolicy.class).layers;
                switch (warpingDestinationEdge) {
                    case OPPOSITE -> oppositeWarp(
                        worldPosition,
                        warpingEdge,
                        worldDimensions,
                        warpLayers
                    );
                    case CLOCKWISE -> clockwiseWarp(
                        worldPosition,
                        warpingEdge,
                        worldDimensions,
                        warpLayers
                    );
                    case COUNTER_CLOCKWISE -> counterClockwiseWarp(
                        worldPosition,
                        warpingEdge,
                        worldDimensions,
                        warpLayers
                    );
                }
            }
        }
    }

    private void oppositeWarp(
        WorldPosition worldPosition,
        WarpingEdge warpingEdge,
        WorldDimensions worldDimensions,
        int warpLayers
    ) {
        switch (warpingEdge) {
            case LEFT -> worldPosition.x = worldDimensions.width() - warpLayers - 1;
            case RIGHT -> worldPosition.x = warpLayers;
            case TOP -> worldPosition.y = warpLayers;
            case BOTTOM -> worldPosition.y = worldDimensions.height() - warpLayers - 1;
        }
    }

    private void clockwiseWarp(
        WorldPosition worldPosition,
        WarpingEdge warpingEdge,
        WorldDimensions worldDimensions,
        int warpLayers
    ) {
        var scaledY = scaledAxis(
            worldPosition.y,
            warpLayers,
            worldDimensions.height(),
            worldDimensions.width()
        );
        var invertedScaledX = invertedAxis(
            scaledAxis(
                worldPosition.x,
                warpLayers,
                worldDimensions.width(),
                worldDimensions.height()
            ),
            warpLayers,
            worldDimensions.height()
        );
        switch (warpingEdge) {
            case LEFT -> {
                worldPosition.x = scaledY;
                worldPosition.y = worldDimensions.height() - warpLayers - 1;
            }
            case RIGHT -> {
                worldPosition.x = scaledY;
                worldPosition.y = warpLayers;
            }
            case TOP -> {
                worldPosition.y = invertedScaledX;
                worldPosition.x = worldDimensions.width() - warpLayers - 1;
            }
            case BOTTOM -> {
                worldPosition.y = invertedScaledX;
                worldPosition.x = warpLayers;
            }
        }
    }

    private void counterClockwiseWarp(
        WorldPosition worldPosition,
        WarpingEdge warpingEdge,
        WorldDimensions worldDimensions,
        int warpLayers
    ) {
        var scaledX = scaledAxis(
            worldPosition.x,
            warpLayers,
            worldDimensions.width(),
            worldDimensions.height()
        );
        var invertedScaledY = invertedAxis(
            scaledAxis(
                worldPosition.y,
                warpLayers,
                worldDimensions.height(),
                worldDimensions.width()
            ),
            warpLayers,
            worldDimensions.width()
        );
        switch (warpingEdge) {
            case LEFT -> {
                worldPosition.x = invertedScaledY;
                worldPosition.y = warpLayers;
            }
            case RIGHT -> {
                worldPosition.x = invertedScaledY;
                worldPosition.y = worldDimensions.height() - warpLayers - 1;
            }
            case TOP -> {
                worldPosition.y = scaledX;
                worldPosition.x = warpLayers;
            }
            case BOTTOM -> {
                worldPosition.y = scaledX;
                worldPosition.x = worldDimensions.width() - warpLayers - 1;
            }
        }
    }

    private int scaledAxis(
        int current,
        int warpLayers,
        int sourceDimension,
        int targetDimension
    ) {
        var sourceRange = sourceDimension - 2 * warpLayers - 1;
        float targetRange = targetDimension - 2 * warpLayers - 1f;
        return Math.round((current - warpLayers) * targetRange / sourceRange)
               + warpLayers;
    }

    private int invertedAxis(
        int scaled,
        int warpLayers,
        int targetDimension
    ) {
        return targetDimension - warpLayers - 1 - scaled + warpLayers;
    }
}
