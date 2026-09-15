package com.github.maximtereshchenko.games.cookies.domain;

public record Interval(double remainingTimeSeconds, double lengthSeconds) {

    public double progress() {
        return remainingTimeSeconds / lengthSeconds;
    }
}
