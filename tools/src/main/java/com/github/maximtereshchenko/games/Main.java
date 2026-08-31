package com.github.maximtereshchenko.games;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;
import com.badlogic.gdx.utils.Json;

final class Main {

    static void main(String[] args) {
        switch (args[0]) {
            case "generateBitmapFont" -> generateBitmapFont(
                args[1],
                args[2]
            );
        }
    }

    private static void generateBitmapFont(
        String fontPath,
        String freeTypeFontParameterJson
    ) {
        var configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setInitialVisible(false);
        new Lwjgl3Application(
            new ApplicationListener() {

                @Override
                public void create() {}

                @Override
                public void resize(int width, int height) {}

                @Override
                public void render() {
                    var fontFile = Gdx.files.absolute(fontPath);
                    var generator = new FreeTypeFontGenerator(fontFile);
                    var freeTypeFontParameter = new Json()
                        .fromJson(
                            FreeTypeFontGenerator.FreeTypeFontParameter.class,
                            freeTypeFontParameterJson
                        );
                    var font = generator.generateFont(freeTypeFontParameter);
                    var regions = font.getRegions();
                    var pixmaps = new Pixmap[regions.size];
                    for (int i = 0; i < regions.size; i++) {
                        var texture = regions.get(i).getTexture();
                        var textureData = texture.getTextureData();
                        if (!textureData.isPrepared()) {
                            textureData.prepare();
                        }
                        pixmaps[i] = textureData.consumePixmap();
                    }
                    var info = new BitmapFontWriter.FontInfo();
                    info.overrideMetrics(font.getData());
                    info.face = fontFile.nameWithoutExtension();
                    info.size = freeTypeFontParameter.size;
                    info.descent = 0;
                    BitmapFontWriter.writeFont(
                        font.getData(),
                        pixmaps,
                        Gdx.files.absolute(
                            "%s/%s.fnt".formatted(
                                fontFile.parent().path(),
                                fontFile.nameWithoutExtension()
                            )
                        ),
                        info
                    );
                    Gdx.app.exit();
                }

                @Override
                public void pause() {}

                @Override
                public void resume() {}

                @Override
                public void dispose() {}
            },
            configuration
        );
    }
}
