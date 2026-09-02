package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

final class CursorWidget extends Image {

    CursorWidget(Skin skin) {
        super(skin, "texture_cursor");
        setOrigin(Align.center);
    }

    void scale(float parentWidth) {
        setScale(parentWidth / (6 * getPrefWidth()));
    }

    void transform(
        int ringIndex,
        int cursorIndex,
        int cursorsPerRing,
        float baseAngleDegrees,
        float accumulatedTimeSeconds,
        float originX,
        float originY
    ) {
        var angleDegrees = angleDegrees(
            ringIndex,
            cursorIndex,
            baseAngleDegrees,
            accumulatedTimeSeconds
        );
        transform(
            angleDegrees,
            originX,
            originY,
            vector2(
                ringIndex,
                cursorIndex,
                cursorsPerRing,
                accumulatedTimeSeconds,
                angleDegrees
            )
        );
    }

    private float angleDegrees(
        int ringIndex,
        int cursorIndex,
        float baseAngleDegrees,
        float accumulatedTimeSeconds
    ) {
        var ringDegrees = baseAngleDegrees * ringIndex / 2;
        var cursorDegrees = baseAngleDegrees * cursorIndex;
        var timeDegrees = 3 * accumulatedTimeSeconds;
        return ringDegrees + cursorDegrees + timeDegrees;
    }

    private Vector2 vector2(
        int ringIndex,
        int cursorIndex,
        int cursorsPerRing,
        float accumulatedTimeSeconds,
        float angleDegrees
    ) {
        var baseDistance = 3.75f * getWidth();
        var ringDistance = ringIndex * getWidth() / 2;
        var clickingDistance = clickingDistance(
            ringIndex,
            cursorIndex,
            cursorsPerRing,
            accumulatedTimeSeconds
        );
        var waveDistance = waveDistance(
            ringIndex,
            accumulatedTimeSeconds
        );
        var radius = baseDistance + ringDistance +
                     clickingDistance + waveDistance;
        return new Vector2(radius, 0).setAngleDeg(angleDegrees);
    }

    private float clickingDistance(
        int ringIndex,
        int cursorIndex,
        int cursorsPerRing,
        float accumulatedTimeSeconds
    ) {
        var timePhase = accumulatedTimeSeconds * 0.75f;
        var sinIndex = cursorIndex + ringIndex * cursorsPerRing / 4f;
        var offsetCursors = cursorsPerRing / 2f;
        var percentage = sinIndex % (offsetCursors) / (offsetCursors);
        var sin = Math.sin(timePhase + percentage * Math.toRadians(360));
        if (sin > 0.997f) {
            return -getWidth() * 0.2f;
        }
        if (sin > 0.994f) {
            return -getWidth() * 0.02f;
        }
        return 0;
    }

    private float waveDistance(
        int ringIndex,
        float accumulatedTimeSeconds
    ) {
        var timePhase = accumulatedTimeSeconds * 0.3f;
        var totalDegrees = (ringIndex + timePhase) * 90f;
        var amplitude = getWidth() * 0.125f;
        return (float) (Math.sin(Math.toRadians(totalDegrees)) * amplitude);
    }

    private void transform(
        float angleDegrees,
        float originX,
        float originY,
        Vector2 vector2
    ) {
        setRotation(angleDegrees + 90);
        setPosition(
            originX + vector2.x * getScaleX() - getWidth() / 2f,
            originY + vector2.y * getScaleY() - getHeight() / 2f
        );
    }
}
