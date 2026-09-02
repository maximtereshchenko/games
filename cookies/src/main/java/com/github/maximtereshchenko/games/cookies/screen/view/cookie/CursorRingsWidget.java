package com.github.maximtereshchenko.games.cookies.screen.view.cookie;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.github.maximtereshchenko.games.common.event.EventBus;
import com.github.maximtereshchenko.games.common.event.Subscriber;
import com.github.maximtereshchenko.games.cookies.domain.Event;
import com.github.maximtereshchenko.games.cookies.domain.Generator;
import com.github.maximtereshchenko.games.cookies.domain.GeneratorBought;

import java.util.ArrayList;
import java.util.List;

final class CursorRingsWidget extends WidgetGroup implements Subscriber<Event> {

    private static final int CURSORS_PER_RING = 50;
    private static final float DEGREES_PER_CURSOR = 360f / CURSORS_PER_RING;

    private final Skin skin;
    private final List<Image> cursors;
    private float accumulatedTimeSeconds;

    CursorRingsWidget(Skin skin, EventBus<Event> eventBus) {
        this.skin = skin;
        this.cursors = new ArrayList<>();
        setLayoutEnabled(false);
        eventBus.subscribe(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        accumulatedTimeSeconds += delta;
        for (var i = 0; i < cursors.size(); i++) {
            var cursor = cursors.get(i);
            var ringIndex = i / CURSORS_PER_RING;
            var cursorIndex = i % CURSORS_PER_RING;
            var angleDegrees = angleDegrees(ringIndex, cursorIndex);
            transform(
                cursor,
                angleDegrees,
                vector2(
                    ringIndex,
                    cursorIndex,
                    cursor.getWidth(),
                    angleDegrees
                )
            );
        }
    }

    @Override
    public void sizeChanged() {
        super.sizeChanged();
        setOrigin(Align.center);
        for (var cursor : cursors) {
            cursor.setScale(
                getWidth() / (6 * cursor.getPrefWidth()) //TODO duplication
            );
        }
    }

    @Override
    public void onEvent(Event event) {
        if (
            event instanceof GeneratorBought generatorBought &&
            generatorBought.generator() == Generator.CURSOR
        ) {
            for (var i = cursors.size(); i < generatorBought.newAmount(); i++) {
                var cursor = new Image(skin, "texture_cursor");
                cursor.setOrigin(Align.center);
                cursor.setScale(
                    getWidth() / (6 * cursor.getPrefWidth())
                );
                cursors.add(cursor);
                addActor(cursor);
            }
        }
    }

    private void transform(
        Image cursor,
        float angleDegrees,
        Vector2 vector2
    ) {
        cursor.setRotation(angleDegrees + 90);
        cursor.setPosition(
            getOriginX() + vector2.x * cursor.getScaleX() - cursor.getWidth() / 2f,
            getOriginY() + vector2.y * cursor.getScaleY() - cursor.getHeight() / 2f
        );
    }

    private float angleDegrees(int ringIndex, int cursorIndex) {
        var ringDegrees = DEGREES_PER_CURSOR * ringIndex / 2;
        var cursorDegrees = DEGREES_PER_CURSOR * cursorIndex;
        var timeDegrees = 3 * accumulatedTimeSeconds;
        return ringDegrees + cursorDegrees + timeDegrees;
    }

    private Vector2 vector2(
        int ringIndex,
        int cursorIndex,
        float cursorWidth,
        float angleDegrees
    ) {
        var baseDistance = 3.75f * cursorWidth;
        var ringDistance = ringIndex * cursorWidth / 2;
        var clickingDistance = clickingDistance(
            ringIndex,
            cursorIndex,
            cursorWidth
        );
        var waveDistance = waveDistance(ringIndex, cursorWidth);
        var radius = baseDistance + ringDistance + clickingDistance + waveDistance;
        return new Vector2(radius, 0).setAngleDeg(angleDegrees);
    }

    private float clickingDistance(
        int ringIndex,
        int cursorIndex,
        float cursorWidth
    ) {
        var timePhase = accumulatedTimeSeconds * 0.75f;
        var sinIndex = cursorIndex + ringIndex * CURSORS_PER_RING / 4f;
        var offsetCursors = CURSORS_PER_RING / 2f;
        var percentage = sinIndex % (offsetCursors) / (offsetCursors);
        var sin = Math.sin(timePhase + percentage * Math.toRadians(360));
        if (sin > 0.997f) {
            return -cursorWidth * 0.2f;
        }
        if (sin > 0.994f) {
            return -cursorWidth * 0.02f;
        }
        return 0;
    }

    private float waveDistance(int ringIndex, float cursorWidth) {
        var timePhase = accumulatedTimeSeconds * 0.3f;
        var totalDegrees = (ringIndex + timePhase) * 90f;
        var amplitude = cursorWidth * 0.125f;
        return (float) (Math.sin(Math.toRadians(totalDegrees)) * amplitude);
    }
}