package dev.gallon.fabric.config;

import dev.gallon.domain.AboveHeadKind;
import dev.gallon.domain.DisplayMinMax;
import dev.gallon.domain.GroupedKind;
import dev.gallon.domain.InteractionKind;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TheModConfigTest {
    @Test
    void emptyConfigUsesEveryDefault() throws Exception {
        Jankson jankson = Jankson.builder().build();
        TheModConfig config = jankson.fromJson("{}", TheModConfig.class);

        config.validatePostLoad();

        assertEquals(InteractionKind.RIGHT_CLICK, config.modConfig.getDisplayStatsOnInteraction());
        assertEquals(GroupedKind.INDIVIDUAL, config.modConfig.getGroupedStats());
        assertEquals(AboveHeadKind.WHEN_LOOKING, config.modConfig.getDisplayStatsAboveHead());
    }

    @Test
    void manuallyEditedValuesAreLoaded() throws Exception {
        Jankson jankson = Jankson.builder().build();
        TheModConfig config = jankson.fromJson(
                """
                        {
                          "modConfig": {
                            "displayStatsInInventory": false,
                            "displayStatsOnInteraction": "MIDDLE_CLICK",
                            "coloredStats": false,
                            "displayMinMax": "MAX_ONLY",
                            "displayStatsInPercentage": true,
                            "groupedStats": "GROUPED_AND_INDIVIDUAL",
                            "includeAttributeModifiers": false,
                            "displayStatsAboveHead": "ALWAYS"
                          }
                        }
                        """,
                TheModConfig.class
        );

        config.validatePostLoad();

        assertFalse(config.modConfig.getDisplayStatsInInventory());
        assertEquals(InteractionKind.MIDDLE_CLICK, config.modConfig.getDisplayStatsOnInteraction());
        assertFalse(config.modConfig.getColoredStats());
        assertEquals(DisplayMinMax.MAX_ONLY, config.modConfig.getDisplayMinMax());
        assertTrue(config.modConfig.getDisplayStatsInPercentage());
        assertEquals(GroupedKind.GROUPED_AND_INDIVIDUAL, config.modConfig.getGroupedStats());
        assertFalse(config.modConfig.getIncludeAttributeModifiers());
        assertEquals(AboveHeadKind.ALWAYS, config.modConfig.getDisplayStatsAboveHead());
    }

    @Test
    void serializedConfigOnlyContainsEditableEntries() {
        Jankson jankson = Jankson.builder().build();

        String json = jankson.toJson(new TheModConfig()).toJson();

        assertFalse(json.contains("DEFAULT"), json);
        assertFalse(json.contains("Defaults"), json);
    }

    @Test
    void resetsLegacyBooleanGroupedStatsToItsDefaultValue() throws Exception {
        Jankson jankson = Jankson.builder().build();
        TheModConfig config = jankson.fromJson(
                """
                        {
                          "modConfig": {
                            "groupedStats": false
                          }
                        }
                        """,
                TheModConfig.class
        );

        config.validatePostLoad();

        assertEquals(GroupedKind.INDIVIDUAL, config.modConfig.getGroupedStats());
    }

    @Test
    void resetsUnknownEnumEntriesToTheirDefaultValues() throws Exception {
        Jankson jankson = Jankson.builder().build();
        TheModConfig config = jankson.fromJson(
                """
                        {
                          "modConfig": {
                            "displayStatsOnInteraction": "REMOVED_VALUE",
                            "groupedStats": "REMOVED_VALUE",
                            "displayStatsAboveHead": "REMOVED_VALUE"
                          }
                        }
                        """,
                TheModConfig.class
        );

        config.validatePostLoad();

        assertEquals(InteractionKind.RIGHT_CLICK, config.modConfig.getDisplayStatsOnInteraction());
        assertEquals(GroupedKind.INDIVIDUAL, config.modConfig.getGroupedStats());
        assertEquals(AboveHeadKind.WHEN_LOOKING, config.modConfig.getDisplayStatsAboveHead());
    }

    @Test
    void truncatedConfigIsRejectedByTheSerializerForAutoConfigToRecover() {
        Jankson jankson = Jankson.builder().build();

        org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class,
                () -> jankson.fromJson("{\"modConfig\": {", TheModConfig.class)
        );
    }
}
