package com.github.maximtereshchenko.games.cookies.screen.view.bakery;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.github.maximtereshchenko.games.cookies.domain.BakeryService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

final class FallingCookiesWidget extends WidgetGroup {

    private final Skin skin;
    private final Style style;
    private final BakeryService bakeryService;
    private final Random random;
    private float offsetPercentage;

    FallingCookiesWidget(
        Skin skin,
        BakeryService bakeryService,
        Random random
    ) {
        this.skin = skin;
        this.style = skin.get(Style.class);
        this.bakeryService = bakeryService;
        this.random = random;
        style.fixTiling();
        setLayoutEnabled(false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawable().ifPresent(drawable -> draw(batch, drawable));
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        offsetPercentage += delta / 10;
        offsetPercentage %= 1;
        if (shouldAdd(delta)) {
            addFallingCookieWidget();
        }
    }

    void addFallingCookieWidget() {
        addActor(new FallingCookieWidget(skin, random, getWidth(), getHeight()));
    }

    private void draw(Batch batch, Drawable drawable) {
        var offset = drawable.getMinHeight() * offsetPercentage;
        drawable.draw(
            batch,
            getX(),
            getY() - offset,
            getWidth(),
            getHeight() + offset
        );
    }

    private boolean shouldAdd(float delta) {
        return random.nextFloat() < Math.min(10, bakeryService.bakingRate().floatValue()) * delta;
    }

    private Optional<Drawable> drawable() {
        var bakingRate = bakeryService.bakingRate();
        if (greaterThan(bakingRate, 1000)) {
            return Optional.ofNullable(style.highAmount);
        }
        if (greaterThan(bakingRate, 500)) {
            return Optional.ofNullable(style.mediumAmount);
        }
        if (greaterThan(bakingRate, 50)) {
            return Optional.ofNullable(style.lowAmount);
        }
        return Optional.empty();
    }

    private boolean greaterThan(BigDecimal bigDecimal, int value) {
        return bigDecimal.compareTo(BigDecimal.valueOf(value)) >= 0;
    }

    private static final class Style {

        TiledDrawable lowAmount;
        TiledDrawable mediumAmount;
        TiledDrawable highAmount;

        void fixTiling() {
            lowAmount.setRegion(lowAmount.getRegion());
            mediumAmount.setRegion(mediumAmount.getRegion());
            highAmount.setRegion(highAmount.getRegion());
        }
    }
}
