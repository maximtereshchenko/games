package com.github.maximtereshchenko.games.snakes.session;

final class WarpingPolicy {

    final int periodConsumedFood;
    int remainingConsumedFood;
    int layers;

    WarpingPolicy(int periodConsumedFood, int remainingConsumedFood, int layers) {
        this.periodConsumedFood = periodConsumedFood;
        this.remainingConsumedFood = remainingConsumedFood;
        this.layers = layers;
    }
}
