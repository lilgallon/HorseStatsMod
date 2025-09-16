package dev.gallon.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record HorseStats(
        @NotNull String name,
        @NotNull Double health,
        @NotNull Double jumpHeight,
        @NotNull Double speed,
        @NotNull Optional<Integer> slots,
        @NotNull Optional<String> owner
) {
    public static final @NotNull Integer MIN_HEALTH = 15;
    public static final @NotNull Integer MAX_HEALTH = 30;
    public static final @NotNull Double MIN_JUMP_HEIGHT = 1.11;
    public static final @NotNull Double MAX_JUMP_HEIGHT = 5.3;
    public static final @NotNull Double MIN_SPEED = 4.74;
    public static final @NotNull Double MAX_SPEED = 14.23;
    public static final @NotNull Integer MIN_SLOTS = 3;
    public static final @NotNull Integer MAX_SLOTS = 15;

    private @NotNull Integer getHealthPercentage() {
        return (int) ((health - MIN_HEALTH) / (MAX_HEALTH - MIN_HEALTH) * 100);
    }

    private @NotNull Integer getJumpHeightPercentage() {
        return (int) ((jumpHeight - MIN_JUMP_HEIGHT) / (MAX_JUMP_HEIGHT - MIN_JUMP_HEIGHT) * 100);
    }

    private @NotNull Integer getSpeedPercentage() {
        return (int) ((speed - MIN_SPEED) / (MAX_SPEED - MIN_SPEED) * 100);
    }

    private @NotNull Integer getSlotsPercentage() {
        return (slots.orElse(MIN_SLOTS) - MIN_SLOTS) / (MAX_SLOTS - MIN_SLOTS) * 100;
    }

    public @NotNull String getHealthStr(Boolean percentage) {
        return percentage ? (getHealthPercentage() + "%" ) : String.format("%.2f", health);
    }

    public @NotNull String getJumpHeightStr(Boolean percentage) {
        return percentage ? (getJumpHeightPercentage() + "%" ) : String.format("%.2f", jumpHeight);
    }

    public @NotNull String getSpeedStr(Boolean percentage) {
        return percentage ? (getSpeedPercentage() + "%" ) : String.format("%.2f", speed);
    }

    public @NotNull String getSlotsStr(Boolean percentage) {
        return percentage ? (getSlotsPercentage() + "%" ) : slots.orElse(0).toString();
    }
}
