package com.github.maximtereshchenko.ecs;

public interface System {

    void update(WorldEdit worldEdit, float deltaTimeSeconds);
}
