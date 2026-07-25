package dev.gallon.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JumpHeightConverterTest {
    @Test
    void handlesEveryNonFiniteValue() {
        assertEquals(1.153, JumpHeightConverter.getJumpHeight(Double.NaN));
        assertEquals(1.153, JumpHeightConverter.getJumpHeight(Double.NEGATIVE_INFINITY));
        assertEquals(5.920, JumpHeightConverter.getJumpHeight(Double.POSITIVE_INFINITY));
    }

    @Test
    void clampsValuesOutsideTheSupportedRange() {
        assertEquals(1.153, JumpHeightConverter.getJumpHeight(-10));
        assertEquals(5.920, JumpHeightConverter.getJumpHeight(10));
    }
}
