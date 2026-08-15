package com.github.maximtereshchenko.games.bricks.session;

final class SpawnedStars {

    final int max;
    int accumulated;

    SpawnedStars(int max, int accumulated) {
        this.max = max;
        this.accumulated = accumulated;
    }
}
