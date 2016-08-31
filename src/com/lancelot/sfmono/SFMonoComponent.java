package com.lancelot.sfmono;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SFMonoComponent implements ApplicationComponent {


    public SFMonoComponent() {
    }

    @Override
    public void initComponent() {
        final String USER_DIR = System.getProperty("user.home");
        final String USER_FONTS_DIR = "Library/Fonts";
        final String[] SF_MONO_PATHS = new String[]{
                "/Applications/Xcode.app/Contents/SharedFrameworks/DVTKit.framework/Versions/A/Resources/Fonts/",
                "/Applications/Xcode-beta.app/Contents/SharedFrameworks/DVTKit.framework/Versions/A/Resources/Fonts/",
                "/Applications/Utilities/Terminal.app/Contents/Resources/Fonts/"
        };
        final String[] SFMONO_REGULAR_FONT_NAMES = {
                "SFMono-Bold.otf",
                "SFMono-BoldItalic.otf",
                "SFMono-Heavy.otf",
                "SFMono-HeavyItalic.otf",
                "SFMono-Light.otf",
                "SFMono-LightItalic.otf",
                "SFMono-Medium.otf",
                "SFMono-MediumItalic.otf",
                "SFMono-Regular.otf",
                "SFMono-RegularItalic.otf",
                "SFMono-Semibold.otf",
                "SFMono-SemiboldItalic.otf"
        };

        int existingCount = 0;
        for (String name : SFMONO_REGULAR_FONT_NAMES) {
            final Path dest = Paths.get(Paths.get(USER_DIR, USER_FONTS_DIR).toString(), name);
            if (Files.exists(dest)) {
                existingCount++;
            }
        }
        if (existingCount == SFMONO_REGULAR_FONT_NAMES.length) {
            updateUI();
            return;
        }

        for (String dir : SF_MONO_PATHS) {
            boolean stopAfter = false;
            for (String name : SFMONO_REGULAR_FONT_NAMES) {
                Path source = Paths.get(dir, name);
                Path dest = Paths.get(Paths.get(USER_DIR, USER_FONTS_DIR).toString(), name);

                if (Files.exists(source) && !Files.exists(dest)) {
                    try {
                        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                        stopAfter = true;
                    } catch (IOException ignored) {
                    }
                }
            }
            if (stopAfter) {
                break;
            }
        }
    }

    private void updateUI() {
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        scheme.setConsoleFontName("SF Mono");
        scheme.setEditorFontName("SF Mono");
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "SF Mono font component";
    }
}
