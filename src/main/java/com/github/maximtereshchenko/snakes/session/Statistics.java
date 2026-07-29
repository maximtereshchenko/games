package com.github.maximtereshchenko.snakes.session;

import java.util.EnumMap;
import java.util.Map;

public final class Statistics {

    public final Map<SessionMetric, Integer> value;

    public Statistics(Map<SessionMetric, Integer> value) {
        this.value = new EnumMap<>(SessionMetric.class);
        for (var sessionStatistics : SessionMetric.values()) {
            this.value.put(
                sessionStatistics,
                value.getOrDefault(sessionStatistics, 0)
            );
        }
    }
}
