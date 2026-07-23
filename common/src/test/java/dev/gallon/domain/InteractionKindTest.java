package dev.gallon.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InteractionKindTest {
    @Test
    void middleClickDoesNotTriggerRightClickInteractions() {
        assertFalse(InteractionKind.MIDDLE_CLICK.matchesRightClick(false));
        assertFalse(InteractionKind.MIDDLE_CLICK.matchesRightClick(true));
    }

    @Test
    void existingRightClickModesKeepTheirBehavior() {
        assertTrue(InteractionKind.RIGHT_CLICK.matchesRightClick(false));
        assertFalse(InteractionKind.RIGHT_CLICK.matchesRightClick(true));
        assertFalse(InteractionKind.SHIFT_RIGHT_CLICK.matchesRightClick(false));
        assertTrue(InteractionKind.SHIFT_RIGHT_CLICK.matchesRightClick(true));
        assertTrue(InteractionKind.RIGHT_OR_SHIFT_RIGHT_CLICK.matchesRightClick(false));
        assertTrue(InteractionKind.RIGHT_OR_SHIFT_RIGHT_CLICK.matchesRightClick(true));
        assertFalse(InteractionKind.DISABLED.matchesRightClick(false));
        assertFalse(InteractionKind.DISABLED.matchesRightClick(true));
    }
}
