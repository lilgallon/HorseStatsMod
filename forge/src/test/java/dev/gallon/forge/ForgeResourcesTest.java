package dev.gallon.forge;

import dev.gallon.domain.I18nKeys;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ForgeResourcesTest {
    private static final List<String> CONFIG_TRANSLATION_KEYS = List.of(
            "text.autoconfig.horsestatsmod.title",
            I18nKeys.DISPLAY_STATS_IN_INVENTORY,
            I18nKeys.DISPLAY_STATS_ON_INTERACTION,
            I18nKeys.COLORED_STATS,
            I18nKeys.DISPLAY_MIN_MAX,
            I18nKeys.STATS_IN_PERCENTAGE,
            I18nKeys.GROUPED_STATS,
            I18nKeys.INCLUDE_ATTRIBUTE_MODIFIERS
    );

    @Test
    void usesTheForge26ResourcePackMetadataFormat() throws IOException {
        String metadata = readResource("/pack.mcmeta");
        String compactMetadata = metadata.replaceAll("\\s+", "");

        assertTrue(metadata.contains("\"description\": \"horsestatsmod resources\""));
        assertTrue(metadata.contains("\"max_format\": 121"));
        assertTrue(compactMetadata.contains("\"min_format\":[121,0]"));
        assertNull(ForgeResourcesTest.class.getResource("/pack.metadata"));
    }

    @Test
    void packagesEveryConfigScreenTranslationInEnglishAndFrench() throws IOException {
        for (String language : List.of("en_us", "fr_fr")) {
            String translations = readResource("/assets/horsestatsmod/lang/" + language + ".json");
            for (String key : CONFIG_TRANSLATION_KEYS) {
                assertTrue(translations.contains("\"" + key + "\""), language + " is missing " + key);
                assertFalse(translations.contains("\"" + key + "\": \"\""), language + " has an empty " + key);
            }
        }
    }

    private static String readResource(String path) throws IOException {
        try (InputStream stream = ForgeResourcesTest.class.getResourceAsStream(path)) {
            assertNotNull(stream, "Missing classpath resource " + path);
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
