package dev.gallon;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MountHintStateTest {
    @Test
    void suppressesOnlyTheFirstHintForEachHorseMount() {
        MountHintState state = new MountHintState();
        UUID horse = UUID.randomUUID();

        assertTrue(state.update(horse, true));
        assertFalse(state.update(horse, true));
        assertFalse(state.update(null, false));
        assertTrue(state.update(horse, true));
    }

    @Test
    void doesNotSuppressOtherVehicleHints() {
        MountHintState state = new MountHintState();
        UUID boat = UUID.randomUUID();
        UUID horse = UUID.randomUUID();

        assertFalse(state.update(boat, false));
        assertTrue(state.update(horse, true));
    }
}
