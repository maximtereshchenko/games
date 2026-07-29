package com.github.maximtereshchenko.snakes.session;

final class SegmentPolicy {

    final int incrementStepTurns;
    int durationTurns;

    SegmentPolicy(int incrementStepTurns, int durationTurns) {
        this.incrementStepTurns = incrementStepTurns;
        this.durationTurns = durationTurns;
    }
}
