package com.github.maximtereshchenko.games.snakes.session;

public final class Session {

    public Status status;

    public Session(Status status) {
        this.status = status;
    }

    Session() {
        this(Status.RUNNING);
    }

    public enum Status {

        RUNNING, ENDED
    }
}
