package dev.gallon.domain;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HorseStatsTest {
    @Test
    void groupedStatsAveragesNormalizedValues() {
        HorseStats stats = horse(
                30.0,
                1.153,
                4.856625
        );

        assertEquals(33, stats.getGroupedStats());
    }

    @Test
    void groupedStatsKeepsBoundaryValues() {
        HorseStats minimumStats = horse(
                15.0,
                1.153,
                4.856625
        );
        HorseStats maximumStats = horse(
                30.0,
                5.9197,
                14.569875
        );

        assertEquals(0, minimumStats.getGroupedStats());
        assertEquals(100, maximumStats.getGroupedStats());
    }

    private HorseStats horse(double health, double jumpHeight, double speed) {
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
