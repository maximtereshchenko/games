package com.github.maximtereshchenko.games.snakes.session;

import java.util.EnumMap;
import java.util.Map;

public final class SessionStatisticsAccumulator {

    public Map<SessionStatistics, Integer> value;

    public SessionStatisticsAccumulator(Map<SessionStatistics, Integer> value) {
        this.value = new EnumMap<>(value);
    }

    SessionStatisticsAccumulator() {
        this.value = new EnumMap<>(SessionStatistics.class);
    }
}
