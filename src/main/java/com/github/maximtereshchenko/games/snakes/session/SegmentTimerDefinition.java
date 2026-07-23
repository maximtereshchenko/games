package com.github.maximtereshchenko.games.snakes.session;

final class SegmentTimerDefinition {

    final int incrementStep;
    int duration;

    SegmentTimerDefinition(int incrementStep, int duration) {
        this.incrementStep = incrementStep;
        this.duration = duration;
    }
}
