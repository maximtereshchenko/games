package com.github.maximtereshchenko.snakes.session;

import java.util.EnumMap;
import java.util.Map;

public final class SessionStatisticsAccumulator {

    public final Map<SessionStatistics, Integer> value;

    public SessionStatisticsAccumulator() {
        this.value = new EnumMap<>(SessionStatistics.class);
        for (var sessionStatistics : SessionStatistics.values()) {
            this.value.put(
                sessionStatistics,
                value.getOrDefault(sessionStatistics, 0)
            );
        }
    }
}
