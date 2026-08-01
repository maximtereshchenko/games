package com.github.maximtereshchenko.snakes.session;

final class WarpingPolicy {

    final int periodConsumedFood;
    int remainingConsumedFood;
    int layers; //TODO separate

    WarpingPolicy(int periodConsumedFood, int remainingConsumedFood, int layers) {
        this.periodConsumedFood = periodConsumedFood;
        this.remainingConsumedFood = remainingConsumedFood;
        this.layers = layers;
    }
}
