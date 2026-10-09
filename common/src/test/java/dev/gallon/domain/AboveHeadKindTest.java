package dev.gallon.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AboveHeadKindTest {
    private static final double NEAR_SQ = 5 * 5;
    private static final double MID_SQ = 40 * 40;
    private static final double FAR_SQ = 70 * 70;

    @Test
    void whenLookingOnlyDisplaysTheLookedAtMountWithinNameTagRange() {
        assertTrue(AboveHeadKind.WHEN_LOOKING.shouldDisplay(true, NEAR_SQ));
        assertTrue(AboveHeadKind.WHEN_LOOKING.shouldDisplay(true, MID_SQ));
        assertFalse(AboveHeadKind.WHEN_LOOKING.shouldDisplay(true, FAR_SQ));
        assertFalse(AboveHeadKind.WHEN_LOOKING.shouldDisplay(false, NEAR_SQ));
    }

    @Test
    void alwaysDisplaysEveryNearbyMount() {
        assertTrue(AboveHeadKind.ALWAYS.shouldDisplay(false, NEAR_SQ));
        assertTrue(AboveHeadKind.ALWAYS.shouldDisplay(true, NEAR_SQ));
        assertFalse(AboveHeadKind.ALWAYS.shouldDisplay(false, MID_SQ));
        assertFalse(AboveHeadKind.ALWAYS.shouldDisplay(true, FAR_SQ));
    }

    @Test
    void disabledNeverDisplays() {
        assertFalse(AboveHeadKind.DISABLED.shouldDisplay(true, NEAR_SQ));
        assertFalse(AboveHeadKind.DISABLED.shouldDisplay(false, NEAR_SQ));
    }
}
