package dev.gallon.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JumpHeightConverterTest {
    @Test
    void matchesTheHeightsListedOnTheWiki() {
        // https://minecraft.wiki/w/Horse#Jump_strength and https://minecraft.wiki/w/Zombie_Horse#Jump_strength
        assertEquals(1.1531, JumpHeightConverter.getJumpHeight(0.4), 1e-4);
        assertEquals(1.7088, JumpHeightConverter.getJumpHeight(0.5), 1e-4);
        assertEquals(2.3676, JumpHeightConverter.getJumpHeight(0.6), 1e-4);
        assertEquals(3.1243, JumpHeightConverter.getJumpHeight(0.7), 1e-4);
        assertEquals(5.9197, JumpHeightConverter.getJumpHeight(1.0), 1e-4);
    }

    @Test
    void increasesSmoothlyUpToTheMaximum() {
        double previous = JumpHeightConverter.getJumpHeight(0.4);
        for (double strength = 0.401; strength <= 1.0; strength += 0.001) {
            double height = JumpHeightConverter.getJumpHeight(strength);
            assertTrue(height > previous, "jump height must increase at " + strength);
            // The old lookup table jumped by almost a block between 0.995 and 1.0
            assertTrue(height - previous < 0.02, "jump height must not jump at " + strength);
            previous = height;
        }
    }

    @Test
    void handlesEveryNonFiniteValue() {
        assertTrue(Double.isNaN(JumpHeightConverter.getJumpHeight(Double.NaN)));
        assertTrue(Double.isNaN(JumpHeightConverter.getJumpHeight(Double.NEGATIVE_INFINITY)));
        assertTrue(Double.isNaN(JumpHeightConverter.getJumpHeight(Double.POSITIVE_INFINITY)));
    }

    @Test
    void handlesValuesOutsideTheVanillaRange() {
        assertEquals(0, JumpHeightConverter.getJumpHeight(-10));
        assertEquals(0, JumpHeightConverter.getJumpHeight(0));
        assertEquals(JumpHeightConverter.getJumpHeight(32), JumpHeightConverter.getJumpHeight(1e300));
    }
}
