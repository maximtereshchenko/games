package com.github.maximtereshchenko.games.bricks.session;

final class PhysicsPolicy {

    final float stepTimeSeconds;
    final float maxFrameTimeSeconds;
    float accumulatedTimeSeconds;

    PhysicsPolicy(
        float stepTimeSeconds,
        float maxFrameTimeSeconds,
        float accumulatedTimeSeconds
    ) {
        this.stepTimeSeconds = stepTimeSeconds;
        this.maxFrameTimeSeconds = maxFrameTimeSeconds;
        this.accumulatedTimeSeconds = accumulatedTimeSeconds;
    }
}
