package com.github.maximtereshchenko.ecs;

final class ComponentTypeIdRegistry extends ClassValue<Integer> {

    private int size = 0;

    @Override
    protected Integer computeValue(Class<?> type) {
        return ++size;
    }
}
