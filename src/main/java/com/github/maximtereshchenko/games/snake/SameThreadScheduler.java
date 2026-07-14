package com.github.maximtereshchenko.games.snake;

import dev.dominion.ecs.api.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

final class SameThreadScheduler implements Scheduler {

    private final List<Runnable> systems = new ArrayList<>();
    private double deltaNanoTime;

    @Override
    public Runnable schedule(Runnable system) {
        systems.add(system);
        return system;
    }

    @Override
    public Runnable[] parallelSchedule(Runnable... systems) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forkAndJoin(Runnable subsystem) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forkAndJoinAll(Runnable... subsystems) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void suspend(Runnable system) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resume(Runnable system) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void tick() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void tick(long deltaNanoTime) {
        this.deltaNanoTime = deltaNanoTime;
        systems.forEach(Runnable::run);
    }

    @Override
    public void tickAtFixedRate(int ticksPerSecond) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double deltaTime() {
        return deltaNanoTime / TimeUnit.SECONDS.toNanos(1);
    }

    @Override
    public boolean shutDown() {
        return true;
    }
}
