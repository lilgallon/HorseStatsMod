package dev.gallon.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record HorseStats(
        @NotNull String name,
        @NotNull Double health,
        @NotNull Double jumpHeight,
        @NotNull Double speed,
        @NotNull Optional<Integer> slots,
        @NotNull Optional<String> owner,
        @NotNull MountType mountType
) {
    public @NotNull Integer minHealth() {
        return switch (mountType) {
            case HORSE -> 15;
            case SKELETON_HORSE -> 15;
            case ZOMBIE_HORSE -> 15;
            case DONKEY -> 15;
            case MULE -> 15;
            case LLAMA -> 15;
            case TRADER_LLAMA -> 20;
            case CAMEL -> 32;
        };
    }

    public @NotNull Integer maxHealth() {
        return switch (mountType) {
            case HORSE -> 30;
            case SKELETON_HORSE -> 30;
            case ZOMBIE_HORSE -> 30;
            case DONKEY -> 30;
            case MULE -> 30;
            case LLAMA -> 30;
            case TRADER_LLAMA -> 20;
            case CAMEL -> 32;
        };
    }

    public @NotNull Double minJumpHeight() {
        return switch (mountType) {
            case HORSE -> 1.25;
            case SKELETON_HORSE -> 1.25;
            case ZOMBIE_HORSE -> 1.25;
            case DONKEY -> 1.25;
            case MULE -> 1.25;
            case LLAMA -> 1.25;
            case TRADER_LLAMA -> 1.25;
            case CAMEL -> 1.875;
        };
    }

    public @NotNull Double maxJumpHeight() {
        return switch (mountType) {
            case HORSE -> 5.25;
            case SKELETON_HORSE -> 5.25;
            case ZOMBIE_HORSE -> 5.25;
            case DONKEY -> 5.25;
            case MULE -> 5.25;
            case LLAMA -> 1.25;
            case TRADER_LLAMA -> 1.25;
            case CAMEL -> 1.875;
        };
    }

    public @NotNull Double minSpeed() {
        return switch (mountType) {
            case HORSE -> 4.74;
            case SKELETON_HORSE -> 4.74;
            case ZOMBIE_HORSE -> 4.74;
            case DONKEY -> 4.74;
            case MULE -> 4.74;
            case LLAMA -> 4.31;
            case TRADER_LLAMA -> 4.31;
            case CAMEL -> 4.84;
        };
    }

    public @NotNull Double maxSpeed() {
        return switch (mountType) {
            case HORSE -> 14.23;
            case SKELETON_HORSE -> 14.23;
            case ZOMBIE_HORSE -> 14.23;
            case DONKEY -> 14.23;
            case MULE -> 14.23;
            case LLAMA -> 4.31;
            case TRADER_LLAMA -> 4.31;
            case CAMEL -> 4.84;
        };
    }

    public @NotNull Integer minSlots() {
        return switch (mountType) {
            case HORSE -> 0;
            case SKELETON_HORSE -> 0;
            case ZOMBIE_HORSE -> 8;
            case DONKEY -> 15;
            case MULE -> 15;
            case LLAMA -> 3;
            case TRADER_LLAMA -> 0;
            case CAMEL -> 0;
        };
    }

    public @NotNull Integer maxSlots() {
        return switch (mountType) {
            case HORSE -> 0;
            case SKELETON_HORSE -> 0;
            case ZOMBIE_HORSE -> 8;
            case DONKEY -> 15;
            case MULE -> 15;
            case LLAMA -> 15;
            case TRADER_LLAMA -> 0;
            case CAMEL -> 0;
        };
    }

    private @NotNull Integer computePercentage(double value, double min, double max) {
        if (max == min) {
            return max == 0 ? 0 : 100;
        } else {
            return (int) ((value - min) / (max - min) * 100);
        }
    }

    private @NotNull Integer getHealthPercentage() {
        return computePercentage(health, minHealth(), maxHealth());
    }

    private @NotNull Integer getJumpHeightPercentage() {
        return computePercentage(jumpHeight, minJumpHeight(), maxJumpHeight());
    }

    private @NotNull Integer getSpeedPercentage() {
        return computePercentage(speed, minSpeed(), maxSpeed());
    }

    private @NotNull Integer getSlotsPercentage() {
        return computePercentage(slots.orElse(minSlots()), minSlots(), maxSlots());
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
