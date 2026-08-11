package com.github.maximtereshchenko.games.bricks.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.maximtereshchenko.games.ecs.*;
import com.github.maximtereshchenko.games.ecs.System;

import java.util.function.Function;

final class WorldRenderingSystem implements System {

    private final Iterable<Entity> rectangleEntities;
    private final Iterable<Entity> circleEntities;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;
    private final World physicsWorld;
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    WorldRenderingSystem(
        Registry registry,
        Viewport viewport,
        ShapeRenderer shapeRenderer, World physicsWorld
    ) {
        this.rectangleEntities = registry.entities(
            new Query().all(Visible.class, WorldPosition.class, Rectangle.class)
        );
        this.circleEntities = registry.entities(
            new Query().all(Visible.class, WorldPosition.class, Circle.class)
        );
        this.viewport = viewport;
        this.shapeRenderer = shapeRenderer;
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void update(RegistryEdit registryEdit, float deltaTimeSeconds) {
        ScreenUtils.clear(Color.CLEAR);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.getColor().set(Color.valueOf("#22004f"));
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        draw(
            rectangleEntities,
            entity -> entity.component(Rectangle.class),
            (vector2, rectangle) -> shapeRenderer.rect(
                vector2.x - rectangle.width / 2,
                vector2.y - rectangle.height / 2,
                rectangle.width,
                rectangle.height
            )
        );
        draw(
            circleEntities,
            entity -> entity.component(Circle.class),
            (vector2, circle) -> shapeRenderer.circle(
                vector2.x,
                vector2.y,
                circle.radius(),
                16
            )
        );
        shapeRenderer.end();
        debugRenderer.render(physicsWorld, viewport.getCamera().combined);
    }

    private <T> void draw(
        Iterable<Entity> entities,
        Function<Entity, T> function,
        DrawFunction<T> drawFunction
    ) {
        for (var entity : entities) {
            var color = entity.component(Visible.class).color();
            var worldPosition = entity.component(WorldPosition.class);
            var component = function.apply(entity);
            shapeRenderer.getColor().set(color);
            drawFunction.apply(worldPosition.vector2(), component);
        }
    }

    @FunctionalInterface
    private interface DrawFunction<T> {

        void apply(Vector2 vector2, T component);
    }
}
