package dev.gallon.forge.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import dev.gallon.domain.DisplayMinMax;
import dev.gallon.domain.GroupedKind;
import dev.gallon.domain.InteractionKind;
import dev.gallon.domain.ModConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TheModConfigTest {
    @BeforeEach
    void loadDefaultConfig() {
        TheModConfig.CLIENT_SPEC.acceptConfig(CommentedConfig.inMemory());
        TheModConfig.bakeConfig();
    }

    @Test
    void loadingAndReloadingBakeAllSevenOptions() {
        TheModConfig.CLIENT.displayStatsInInventory.set(false);
        TheModConfig.CLIENT.displayStatsOnInteraction.set(InteractionKind.MIDDLE_CLICK);
        TheModConfig.CLIENT.coloredStats.set(false);
        TheModConfig.CLIENT.displayMinMax.set(DisplayMinMax.MIN_AND_MAX);
        TheModConfig.CLIENT.statsInPercentage.set(true);
        TheModConfig.CLIENT.groupedStats.set(GroupedKind.GROUPED_AND_INDIVIDUAL);
        TheModConfig.CLIENT.includeAttributeModifiers.set(false);

        TheModConfig.bakeConfig();

        assertFalse(TheModConfig.config.getDisplayStatsInInventory());
        assertEquals(InteractionKind.MIDDLE_CLICK, TheModConfig.config.getDisplayStatsOnInteraction());
        assertFalse(TheModConfig.config.getColoredStats());
        assertEquals(DisplayMinMax.MIN_AND_MAX, TheModConfig.config.getDisplayMinMax());
        assertTrue(TheModConfig.config.getDisplayStatsInPercentage());
        assertEquals(GroupedKind.GROUPED_AND_INDIVIDUAL, TheModConfig.config.getGroupedStats());
        assertFalse(TheModConfig.config.getIncludeAttributeModifiers());
    }

    @Test
    void saveUpdatesTheNativeSpecAndTheCommonConfig() {
        ModConfig draft = nonDefaultConfig();

        TheModConfig.applyAndSave(draft);

        assertEquals(false, TheModConfig.CLIENT.displayStatsInInventory.get());
        assertEquals(InteractionKind.SHIFT_RIGHT_CLICK, TheModConfig.CLIENT.displayStatsOnInteraction.get());
        assertEquals(false, TheModConfig.CLIENT.coloredStats.get());
        assertEquals(DisplayMinMax.MAX_ONLY, TheModConfig.CLIENT.displayMinMax.get());
        assertEquals(true, TheModConfig.CLIENT.statsInPercentage.get());
        assertEquals(GroupedKind.GROUPED, TheModConfig.CLIENT.groupedStats.get());
        assertEquals(false, TheModConfig.CLIENT.includeAttributeModifiers.get());

        assertFalse(TheModConfig.config.getDisplayStatsInInventory());
        assertEquals(InteractionKind.SHIFT_RIGHT_CLICK, TheModConfig.config.getDisplayStatsOnInteraction());
        assertFalse(TheModConfig.config.getColoredStats());
        assertEquals(DisplayMinMax.MAX_ONLY, TheModConfig.config.getDisplayMinMax());
        assertTrue(TheModConfig.config.getDisplayStatsInPercentage());
        assertEquals(GroupedKind.GROUPED, TheModConfig.config.getGroupedStats());
        assertFalse(TheModConfig.config.getIncludeAttributeModifiers());
    }

    @Test
    void resetRestoresTheSevenDefaults() {
        TheModConfig.applyAndSave(nonDefaultConfig());

        TheModConfig.applyAndSave(new ModConfig());

        assertTrue(TheModConfig.config.getDisplayStatsInInventory());
        assertEquals(InteractionKind.RIGHT_CLICK, TheModConfig.config.getDisplayStatsOnInteraction());
        assertTrue(TheModConfig.config.getColoredStats());
        assertEquals(DisplayMinMax.DISABLED, TheModConfig.config.getDisplayMinMax());
        assertFalse(TheModConfig.config.getDisplayStatsInPercentage());
        assertEquals(GroupedKind.INDIVIDUAL, TheModConfig.config.getGroupedStats());
        assertTrue(TheModConfig.config.getIncludeAttributeModifiers());
    }

    private static ModConfig nonDefaultConfig() {
        ModConfig config = new ModConfig();
        config.setDisplayStatsInInventory(false);
        config.setDisplayStatsOnInteraction(InteractionKind.SHIFT_RIGHT_CLICK);
        config.setColoredStats(false);
        config.setDisplayMinMax(DisplayMinMax.MAX_ONLY);
        config.setDisplayStatsInPercentage(true);
        config.setGroupedStats(GroupedKind.GROUPED);
        config.setIncludeAttributeModifiers(false);
        return config;
    }
}
