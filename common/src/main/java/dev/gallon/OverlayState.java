package dev.gallon;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

final class OverlayState {
    static final int DISPLAY_TICKS = 60;

    private @NotNull Optional<Component> message = Optional.empty();
    private int remainingTicks;

    void show(@NotNull Component message) {
        this.message = Optional.of(message);
        this.remainingTicks = DISPLAY_TICKS;
    }

    void tick() {
        if (remainingTicks <= 0) {
            return;
        }

        remainingTicks--;
        if (remainingTicks == 0) {
            message = Optional.empty();
        }
    }

    void clear() {
        message = Optional.empty();
        remainingTicks = 0;
    }

    @NotNull Optional<Component> message() {
        return message;
    }

    int remainingTicks() {
        return remainingTicks;
    }
}
