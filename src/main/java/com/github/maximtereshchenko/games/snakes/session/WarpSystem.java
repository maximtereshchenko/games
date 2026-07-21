package com.github.maximtereshchenko.games.snakes.session;

import com.github.maximtereshchenko.games.snakes.Mode;
import dev.dominion.ecs.api.Dominion;

import java.util.function.IntBinaryOperator;

final class WarpSystem extends TurnBasedSystem {

    static final int WARP_LAYER_SIZE = 1;

    private final Dominion dominion;
    private final Mode mode;

    WarpSystem(Dominion dominion, Mode mode) {
        super(dominion);
        this.dominion = dominion;
        this.mode = mode;
    }

    @Override
    void onTurnStarted() {
        for (var warpResults : dominion.findEntitiesWith(Warp.class, Position.class)) {
            for (var headResults : dominion.findEntitiesWith(Head.class, Position.class, CurrentForwardDirection.class, NextForwardDirection.class)) {
                var position = headResults.comp2();
                if (!warpResults.comp2().equals(position)) {
                    continue;
                }
                for (var worldDimensions : dominion.findCompositionsWith(WorldDimensions.class)) {
                    warp(worldDimensions, position, headResults.comp3(), headResults.comp4());
                }
            }
        }
    }

    private void warp(
        WorldDimensions worldDimensions,
        Position position,
        CurrentForwardDirection currentForwardDirection,
        NextForwardDirection nextForwardDirection
    ) {
        if (position.x == 0) {
            warpFromHorizontal(
                worldDimensions,
                position,
                WARP_LAYER_SIZE,
                playableHeight(worldDimensions),
                Integer::sum
            );
        } else if (position.x == worldDimensions.width() - WARP_LAYER_SIZE) {
            warpFromHorizontal(
                worldDimensions,
                position,
                playableHeight(worldDimensions),
                WARP_LAYER_SIZE,
                (a, b) -> a - b
            );
        } else if (position.y == 0) {
            warpFromVertical(
                worldDimensions,
                position,
                playableWidth(worldDimensions),
                WARP_LAYER_SIZE,
                Integer::sum
            );
        } else if (position.y == worldDimensions.height() - WARP_LAYER_SIZE) {
            warpFromVertical(
                worldDimensions,
                position,
                WARP_LAYER_SIZE,
                playableWidth(worldDimensions),
                (a, b) -> a - b
            );
        }
        changeDirection(currentForwardDirection, nextForwardDirection);
    }

    private void warpFromHorizontal(
        WorldDimensions worldDimensions,
        Position position,
        int leftY,
        int rightY,
        IntBinaryOperator operator
    ) {
        switch (mode.warpEdge()) {
            case LEFT -> {
                position.x = worldDimensions.width() -
                             WARP_LAYER_SIZE -
                             scaled(
                                 position.y,
                                 playableHeight(worldDimensions),
                                 playableWidth(worldDimensions)
                             );
                position.y = leftY;
            }
            case RIGHT -> {
                position.x = scaled(
                    position.y,
                    playableHeight(worldDimensions),
                    playableWidth(worldDimensions)
                );
                position.y = rightY;
            }
            case OPPOSITE -> position.x = operator.applyAsInt(position.x, playableWidth(worldDimensions));
        }
    }

    private void warpFromVertical(
        WorldDimensions worldDimensions,
        Position position,
        int leftX,
        int rightX,
        IntBinaryOperator operator
    ) {
        switch (mode.warpEdge()) {
            case LEFT -> {
                position.y = scaled(
                    position.x,
                    playableWidth(worldDimensions),
                    playableHeight(worldDimensions)
                );
                position.x = leftX;
            }
            case RIGHT -> {
                position.y = worldDimensions.height() -
                             WARP_LAYER_SIZE -
                             scaled(
                                 position.x,
                                 playableWidth(worldDimensions),
                                 playableHeight(worldDimensions)
                             );
                position.x = rightX;
            }
            case OPPOSITE -> position.y = operator.applyAsInt(position.y, playableHeight(worldDimensions));
        }
    }

    private int scaled(int original, int max, int targetMax) {
        return Math.floorDiv((original - WARP_LAYER_SIZE) * targetMax, max) + WARP_LAYER_SIZE;
    }

    private int playableWidth(WorldDimensions worldDimensions) {
        return worldDimensions.width() - 2 * WARP_LAYER_SIZE;
    }

    private int playableHeight(WorldDimensions worldDimensions) {
        return worldDimensions.height() - 2 * WARP_LAYER_SIZE;
    }

    private void changeDirection(
        CurrentForwardDirection currentForwardDirection,
        NextForwardDirection nextForwardDirection
    ) {
        currentForwardDirection.value = currentForwardDirection.value
            .relative(
                switch (mode.warpEdge()) {
                    case LEFT -> RelativeDirection.RIGHT;
                    case RIGHT -> RelativeDirection.LEFT;
                    case OPPOSITE -> RelativeDirection.SAME;
                }
            );
        nextForwardDirection.value = currentForwardDirection.value;
    }
}
