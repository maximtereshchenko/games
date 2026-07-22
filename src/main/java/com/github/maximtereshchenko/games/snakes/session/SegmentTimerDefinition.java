package com.github.maximtereshchenko.games.snakes.session;

import java.util.Objects;

final class SegmentTimerDefinition {

    final int step;
    int value;

    SegmentTimerDefinition(int step, int value) {
        this.step = step;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof SegmentTimerDefinition that &&
               step == that.step &&
               value == that.value;
    }
}
