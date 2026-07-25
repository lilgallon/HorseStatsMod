package dev.gallon;

import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OverlayStateTest {
    @Test
    void expiresAfterSixtyTicks() {
        OverlayState state = new OverlayState();
        state.show(Component.literal("Horse stats"));

        for (int tick = 0; tick < OverlayState.DISPLAY_TICKS - 1; tick++) {
            state.tick();
        }

        assertTrue(state.message().isPresent());
        assertEquals(1, state.remainingTicks());

        state.tick();

        assertTrue(state.message().isEmpty());
        assertEquals(0, state.remainingTicks());
    }

    @Test
    void showingAnotherMessageRestartsTheTimer() {
        OverlayState state = new OverlayState();
        state.show(Component.literal("First"));
        state.tick();
        state.show(Component.literal("Second"));

        assertEquals("Second", state.message().orElseThrow().getString());
        assertEquals(OverlayState.DISPLAY_TICKS, state.remainingTicks());
    }
}
