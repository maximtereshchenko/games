package com.github.maximtereshchenko.snakes.session;

final class TurnTimer {

    final float turnLengthSeconds;
    float timePassedSeconds;

    TurnTimer(float turnLengthSeconds, float timePassedSeconds) {
        this.turnLengthSeconds = turnLengthSeconds;
        this.timePassedSeconds = timePassedSeconds;
    }
}
