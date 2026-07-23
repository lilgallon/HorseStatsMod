package dev.gallon.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HorseStatsServiceTest {
    @Test
    void choosesBetweenBaseAndModifiedAttributeValues() {
        assertEquals(10, HorseStatsService.selectAttributeValue(10, 15, false));
        assertEquals(15, HorseStatsService.selectAttributeValue(10, 15, true));
    }
}
