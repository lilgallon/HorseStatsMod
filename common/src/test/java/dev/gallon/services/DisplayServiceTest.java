package dev.gallon.services;

import dev.gallon.domain.GroupedKind;
import dev.gallon.domain.HorseStats;
import dev.gallon.domain.ModConfig;
import dev.gallon.domain.MountType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DisplayServiceTest {
    @Test
    void overlayRespectsTheColoredStatsSetting() {
        ModConfig config = new ModConfig();
        config.setColoredStats(false);

        String message = DisplayService.buildOverlayMessage(config, stats()).getString();

        assertFalse(message.contains("\u00a7"));
    }

    @Test
    void combinedOverlayDisplaysTheOwnerOnlyOnce() {
        ModConfig config = new ModConfig();
        config.setGroupedStats(GroupedKind.GROUPED_AND_INDIVIDUAL);

        String message = DisplayService.buildOverlayMessage(config, stats()).getString();

        assertEquals(1, occurrences(message, "Alice"));
    }

    private static int occurrences(String value, String searched) {
        return (value.length() - value.replace(searched, "").length()) / searched.length();
    }

    private static HorseStats stats() {
        return new HorseStats(
                "Horse",
                20.0,
                3.0,
                10.0,
                Optional.empty(),
                Optional.of("Alice"),
                MountType.HORSE
        );
    }
}
