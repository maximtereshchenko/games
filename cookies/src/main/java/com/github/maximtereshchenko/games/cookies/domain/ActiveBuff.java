package com.github.maximtereshchenko.games.cookies.domain;

abstract class ActiveBuff {

    private float remainingSeconds;

    ActiveBuff() {
        this.remainingSeconds = 0;
    }

    final void update(float deltaTimeSeconds) {
        remainingSeconds = Math.max(
            0,
            remainingSeconds - deltaTimeSeconds
        );
    }

    final Interval interval() {
        return new Interval(remainingSeconds, durationSeconds());
    }

    void reset() {
        remainingSeconds = durationSeconds();
    }

    abstract float durationSeconds();

    abstract BuffDescription buffDescription();
}
