package com.github.maximtereshchenko.ecs;

final class NewEntityIds {

    private final DenseIntArray freeIds = new DenseIntArray();
    private int nextNewId = 1;

    int next() {
        if (freeIds.size() == 0) {
            return nextNewId++;
        }
        var id = freeIds.element(0);
        freeIds.remove(0);
        return id;
    }

    void free(int id) {
        freeIds.set(freeIds.size(), id);
    }
}
