package com.github.maximtereshchenko.games;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.Json;

final class Main {

    static void main(String[] args) {
        switch (args[0]) {
            case "generateBitmapFont" -> generateBitmapFont(
                args[1],
                args[2]
            );
            case "slice" -> slice(
                args[1],
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                args[4]
            );
            case "generateEmptyPixel" -> generateEmptyPixel(
                args[1]
            );
            case "generateTextureAtlas" -> generateTextureAtlas(
                args[1],
                args[2]
            );
        }
    }

    private static void generateTextureAtlas(String directoryPath, String fileName) {
        TexturePacker.process(directoryPath, directoryPath, fileName);
    }

    private static void generateEmptyPixel(String path) {
        GdxNativesLoader.load();
        var pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        PixmapIO.writePNG(
            new FileHandle(path),
            pixmap
        );
    }

    private static void slice(String imagePath, int width, int height, String directoryPath) {
        var imageFile = new FileHandle(imagePath);
        GdxNativesLoader.load();
        var fullPixmap = new Pixmap(new FileHandle(imagePath));
        var index = 0;
        for (var row = 0; row < fullPixmap.getHeight(); row += height) {
            for (var column = 0; column < fullPixmap.getWidth(); column += width) {
                var pixmap = new Pixmap(width, height, fullPixmap.getFormat());
                pixmap.drawPixmap(
                    fullPixmap,
                    column, row, width, height,
                    0, 0, width, height
                );
                if (!isPixmapBlank(pixmap)) {
                    PixmapIO.writePNG(
                        new FileHandle(
                            "%s/%s%d.%s".formatted(
                                directoryPath,
                                imageFile.nameWithoutExtension(),
                                index++,
                                imageFile.extension()
                            )
                        ),
                        pixmap
                    );
                }
            }
        }
    }

    private static boolean isPixmapBlank(Pixmap pixmap) {
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                int pixel = pixmap.getPixel(x, y);
                if ((pixel & 0x000000ff) != 0) {
                    return false;
                }
            }
        }
        return true;
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
