package com.github.maximtereshchenko.games.ecs;

final class ComponentTypeIdRegistry extends ClassValue<Integer> {

    private int size = 0;

    @Override
    protected Integer computeValue(Class<?> type) {
        return ++size;
    }
}
