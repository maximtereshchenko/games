package com.github.maximtereshchenko.games.cookies.domain;

public record Interval(float remainingSeconds, float durationSeconds) {

    public double progress() {
        return (durationSeconds - remainingSeconds) / durationSeconds;
    }
}
