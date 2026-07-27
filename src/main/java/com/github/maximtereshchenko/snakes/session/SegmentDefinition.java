package com.github.maximtereshchenko.snakes.session;

final class SegmentDefinition {

    final int incrementStepTurns;
    int durationTurns;

    SegmentDefinition(int incrementStepTurns, int durationTurns) {
        this.incrementStepTurns = incrementStepTurns;
        this.durationTurns = durationTurns;
    }
}
