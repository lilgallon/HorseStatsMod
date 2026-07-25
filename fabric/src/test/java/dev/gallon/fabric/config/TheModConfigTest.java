package dev.gallon.fabric.config;

import dev.gallon.domain.GroupedKind;
import dev.gallon.domain.InteractionKind;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TheModConfigTest {
    @Test
    void emptyConfigUsesEveryDefault() throws Exception {
        Jankson jankson = Jankson.builder().build();
        TheModConfig config = jankson.fromJson("{}", TheModConfig.class);

        config.validatePostLoad();

        assertEquals(InteractionKind.RIGHT_CLICK, config.modConfig.getDisplayStatsOnInteraction());
        assertEquals(GroupedKind.INDIVIDUAL, config.modConfig.getGroupedStats());
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
                            "groupedStats": "REMOVED_VALUE"
                          }
                        }
                        """,
                TheModConfig.class
        );

        config.validatePostLoad();

        assertEquals(InteractionKind.RIGHT_CLICK, config.modConfig.getDisplayStatsOnInteraction());
        assertEquals(GroupedKind.INDIVIDUAL, config.modConfig.getGroupedStats());
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
