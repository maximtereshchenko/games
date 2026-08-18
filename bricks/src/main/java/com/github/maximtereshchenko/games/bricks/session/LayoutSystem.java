package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.bricks.configuration.*;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.List;

final class LayoutSystem implements System {

    private final Iterable<Entity> entities;
    private final List<List<CellDefinition>> cellDefinitions;
    private final Blueprints blueprints;
    private final Viewport viewport;

    LayoutSystem(
        Registry registry,
        List<List<CellDefinition>> cellDefinitions,
        Blueprints blueprints,
        Viewport viewport
    ) {
        this.entities = registry.entities(
            new Query().all(LayoutPolicy.class)
        );
        this.cellDefinitions = cellDefinitions;
        this.blueprints = blueprints;
        this.viewport = viewport;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        for (var entity : entities) {
            var layoutPolicy = entity.component(LayoutPolicy.class);
            var columns = columns(layoutPolicy);
            var width = viewport.getWorldWidth() / columns;
            var rectangleHalfWidth = width / 2 - layoutPolicy.padding();
            var rectangle = new Rectangle(rectangleHalfWidth, rectangleHalfWidth);
            for (var rowIndex = 0; rowIndex < rows(layoutPolicy); rowIndex++) {
                for (var columnIndex = 0; columnIndex < columns; columnIndex++) {
                    createEntity(
                        registryEdit,
                        width,
                        columnIndex,
                        rowIndex,
                        layoutPolicy,
                        rectangle
                    );
                }
            }
        }
    }

    private void createEntity(
        RegistryEdit registryEdit,
        float width,
        int columnIndex,
        int rowIndex,
        LayoutPolicy layoutPolicy,
        Rectangle rectangle
    ) {
        var worldPosition = worldPosition(width, columnIndex, rowIndex);
        switch (cellDefinition(columnIndex, rowIndex, layoutPolicy)) {
            case BrickDefinition brickDefinition -> registryEdit.addComponents(
                registryEdit.createEntity(),
                blueprints.components(
                    BricksBlueprints.BRICK,
                    rectangle,
                    worldPosition,
                    brickDefinition.color()
                )
            );
            case WallDefinition _ -> registryEdit.addComponents(
                registryEdit.createEntity(),
                blueprints.components(
                    BricksBlueprints.WALL,
                    rectangle,
                    worldPosition
                )
            );
            case EmptyCellDefinition _ -> {
                //empty
            }
        }
    }

    private CellDefinition cellDefinition(
        int columnIndex,
        int rowIndex,
        LayoutPolicy layoutPolicy
    ) {
        return cellDefinitions.get(rowIndex / layoutPolicy.entitiesPerCellSide())
            .get(columnIndex / layoutPolicy.entitiesPerCellSide());
    }

    private WorldPosition worldPosition(
        float width,
        int columnIndex,
        int rowIndex
    ) {
        return new WorldPosition(
            new Vector2(
                width * columnIndex + width / 2,
                viewport.getWorldHeight() - width * rowIndex - width / 2
            )
        );
    }

    private int rows(LayoutPolicy layoutPolicy) {
        return cellDefinitions.size() *
               layoutPolicy.entitiesPerCellSide();
    }

    private int columns(LayoutPolicy layoutPolicy) {
        return cellDefinitions.getFirst().size() *
               layoutPolicy.entitiesPerCellSide();
    }
}
