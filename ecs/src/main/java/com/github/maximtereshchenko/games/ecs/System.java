package com.github.maximtereshchenko.games.ecs;

public interface System {

    void update(WorldEdit worldEdit, float deltaTimeSeconds);
}
