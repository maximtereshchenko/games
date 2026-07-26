package com.github.maximtereshchenko.snakes.session;

public final class Session {

    public Status status;

    public Session(Status status) {
        this.status = status;
    }

    public enum Status {

        RUNNING, ENDED
    }
}
