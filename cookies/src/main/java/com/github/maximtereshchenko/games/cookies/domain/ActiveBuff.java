package com.github.maximtereshchenko.games.cookies.domain;

final class ActiveBuff {

    private float remainingSeconds;
    private float durationSeconds;
    private BuffEffect buffEffect;

    ActiveBuff(BuffEffect buffEffect) {
        this.remainingSeconds = 0;
        this.durationSeconds = 0;
        this.buffEffect = buffEffect;
    }

    void update(float deltaTimeSeconds) {
        remainingSeconds = Math.max(
            0,
            remainingSeconds - deltaTimeSeconds
        );
    }

    Interval interval() {
        return new Interval(remainingSeconds, durationSeconds);
    }

    void reset(BuffEffect buffEffect, float durationSeconds) {
        this.buffEffect = buffEffect;
        this.durationSeconds = durationSeconds;
        remainingSeconds = durationSeconds;
    }

    float durationSeconds() {
        return durationSeconds;
    }

    BuffEffect buffEffect() {
        return buffEffect;
    }
}
