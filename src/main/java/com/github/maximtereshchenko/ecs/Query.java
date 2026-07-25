package com.github.maximtereshchenko.ecs;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Query {

    private final Set<Class<?>> all = new HashSet<>();
    private final Set<Class<?>> one = new HashSet<>();
    private final Set<Class<?>> none = new HashSet<>();

    public Query all(Class<?>... componentTypes) {
        all.addAll(List.of(componentTypes));
        return this;
    }

    public Query one(Class<?>... componentTypes) {
        one.addAll(List.of(componentTypes));
        return this;
    }

    public Query none(Class<?>... componentTypes) {
        none.addAll(List.of(componentTypes));
        return this;
    }

    ViewMask viewMask(ComponentTypeIdRegistry componentTypeIdRegistry) {
        return new ViewMask(
            mask(componentTypeIdRegistry, all),
            mask(componentTypeIdRegistry, one),
            mask(componentTypeIdRegistry, none)
        );
    }

    private BitSet mask(ComponentTypeIdRegistry componentTypeIdRegistry, Set<Class<?>> set) {
        var mask = new BitSet();
        for (var type : set) {
            mask.set(componentTypeIdRegistry.get(type));
        }
        return mask;
    }
}
