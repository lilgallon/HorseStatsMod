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
    public static final @NotNull Double MIN_JUMP_HEIGHT = 1.25;
    public static final @NotNull Double MAX_JUMP_HEIGHT = 5.0;
    public static final @NotNull Double MIN_SPEED = 4.8;
    public static final @NotNull Double MAX_SPEED = 14.5;
    public static final @NotNull Integer MIN_SLOTS = 3;
    public static final @NotNull Integer MAX_SLOTS = 15;
}
