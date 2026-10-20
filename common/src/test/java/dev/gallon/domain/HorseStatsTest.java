package dev.gallon.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HorseStatsTest {
    private Locale defaultLocale;

    @BeforeEach
    void useAKnownDecimalSeparator() {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @AfterEach
    void restoreTheDefaultLocale() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    void groupedStatsAveragesNormalizedValues() {
        HorseStats stats = horse(
                30.0,
                minimum().minJumpHeight(),
                minimum().minSpeed()
        );

        assertEquals(100.0 / 3, stats.getGroupedStats(), 1e-9);
    }

    @Test
    void groupedStatsKeepsBoundaryValues() {
        HorseStats minimumStats = minimum();
        HorseStats maximumStats = horse(30.0, minimumStats.maxJumpHeight(), minimumStats.maxSpeed());

        assertEquals(0, minimumStats.getGroupedStats(), 1e-9);
        assertEquals(100, maximumStats.getGroupedStats(), 1e-9);
    }

    @Test
    void displaysExactlyZeroAndHundredPercentAtTheBoundaries() {
        ModConfig config = percentageConfig(false);
        HorseStats minimumStats = minimum();
        HorseStats maximumStats = horse(30.0, minimumStats.maxJumpHeight(), minimumStats.maxSpeed());

        assertEquals("0%", minimumStats.getHealthStr(config));
        assertEquals("0%", minimumStats.getJumpHeightStr(config));
        assertEquals("0%", minimumStats.getSpeedStr(config));
        assertEquals("100%", maximumStats.getHealthStr(config));
        assertEquals("100%", maximumStats.getJumpHeightStr(config));
        assertEquals("100%", maximumStats.getSpeedStr(config));
    }

    @Test
    void roundsPercentagesToTheNearestValue() {
        // 15 + 0.656 * 15 = 24.84 -> 65.6%
        HorseStats stats = horse(24.84, minimum().minJumpHeight(), minimum().minSpeed());

        assertEquals("66%", stats.getHealthStr(percentageConfig(false)));
        assertEquals("65.6%", stats.getHealthStr(percentageConfig(true)));
    }

    @Test
    void roundsHalfUp() {
        assertEquals("66", HorseStats.formatDecimals(65.5, 0));
        assertEquals("65", HorseStats.formatDecimals(65.49, 0));
        assertEquals("65.5", HorseStats.formatDecimals(65.45, 1));
        assertEquals("2.68", HorseStats.formatDecimals(2.675, 2));
        assertEquals("100", HorseStats.formatDecimals(99.5, 0));
    }

    @Test
    void neverDisplaysNegativeZero() {
        // Spawned horses' minimum speed is a tiny bit lower than the computed minimum because of float rounding
        HorseStats stats = horse(15.0, minimum().minJumpHeight(), minimum().minSpeed() - 1e-9);

        assertEquals("0%", stats.getSpeedStr(percentageConfig(false)));
        assertEquals("0.0%", stats.getSpeedStr(percentageConfig(true)));
    }

    @Test
    void displaysTheConfiguredNumberOfDecimals() {
        ModConfig config = new ModConfig();
        HorseStats stats = horse(23.456789, 3.123456, 11.987654);

        assertEquals("23", stats.getHealthStr(config));
        assertEquals("3.12", stats.getJumpHeightStr(config));
        assertEquals("11.99", stats.getSpeedStr(config));

        config.setHealthDecimals(5);
        config.setJumpHeightDecimals(0);
        config.setSpeedDecimals(3);

        assertEquals("23.45679", stats.getHealthStr(config));
        assertEquals("3", stats.getJumpHeightStr(config));
        assertEquals("11.988", stats.getSpeedStr(config));
    }

    @Test
    void minAndMaxMatchTheVanillaRanges() {
        HorseStats stats = minimum();

        assertEquals(15, stats.minHealth());
        assertEquals(30, stats.maxHealth());
        assertEquals(1.1531, stats.minJumpHeight(), 1e-4);
        assertEquals(5.9197, stats.maxJumpHeight(), 1e-4);
        assertEquals(0.1125 * 43.17, stats.minSpeed(), 1e-6);
        assertEquals(0.3375 * 43.17, stats.maxSpeed(), 1e-6);
    }

    private static ModConfig percentageConfig(boolean withDecimal) {
        ModConfig config = new ModConfig();
        config.setDisplayStatsInPercentage(true);
        config.setPercentageWithDecimal(withDecimal);
        return config;
    }

    private static HorseStats minimum() {
        HorseStats any = horse(15.0, 0, 0);
        return horse(15.0, any.minJumpHeight(), any.minSpeed());
    }

    private static HorseStats horse(double health, double jumpHeight, double speed) {
        return new HorseStats(
                "Test horse",
                health,
                jumpHeight,
                speed,
                Optional.empty(),
                Optional.empty(),
                MountType.HORSE
        );
    }
}
