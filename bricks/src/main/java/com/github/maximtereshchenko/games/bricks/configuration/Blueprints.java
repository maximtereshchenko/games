package com.github.maximtereshchenko.games.bricks.configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Blueprints {

    private final Map<String, List<Object>> components;
    private final Map<Class<?>, UnaryOperator<Object>> copyFunctions;

    private Blueprints(
        Map<String, List<Object>> components,
        Map<Class<?>, UnaryOperator<Object>> copyFunctions
    ) {
        this.components = components;
        this.copyFunctions = copyFunctions;
    }

    public Object[] components(String blueprint, Object... extraComponents) {
        var source = components.get(blueprint);
        var result = new Object[source.size() + extraComponents.length];
        var index = 0;
        for (var component : source) {
            result[index++] = copyFunctions.getOrDefault(
                    component.getClass(),
                    UnaryOperator.identity()
                )
                .apply(component);
        }
        System.arraycopy(
            extraComponents,
            0,
            result,
            index,
            extraComponents.length
        );
        return result;
    }

    public Blueprints merged(Map<String, List<Object>> components) {
        return new Blueprints(
            Stream.of(
                    this.components,
                    components
                )
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(
                    Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (first, second) -> Stream.of(first, second)
                            .flatMap(Collection::stream)
                            .toList()
                    )
                ),
            copyFunctions
        );
    }

    public static final class Builder {

        private final Map<String, List<Object>> components;
        private final Map<Class<?>, UnaryOperator<Object>> copyFunctions;

        public Builder(Map<String, List<Object>> components) {
            this.components = components;
            this.copyFunctions = new HashMap<>();
        }

        public <T> Builder copyFunction(
            Class<T> type,
            UnaryOperator<T> operator
        ) {
            copyFunctions.put(
                type,
                object -> operator.apply(type.cast(object))
            );
            return this;
        }

        public Blueprints build() {
            return new Blueprints(components, copyFunctions);
        }
    }
}
