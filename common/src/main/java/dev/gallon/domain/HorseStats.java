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
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE, LLAMA, TRADER_LLAMA, DONKEY -> 15;
            case CAMEL -> 32;
        };
    }

    public @NotNull Integer maxHealth() {
        return switch (mountType) {
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE, LLAMA, TRADER_LLAMA, DONKEY -> 30;
            case CAMEL -> 32;
        };
    }

    public @NotNull Double minJumpHeight() {
        return switch (mountType) {
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE -> 1.08;
            case DONKEY -> 1.62;
            case LLAMA, TRADER_LLAMA, CAMEL -> 0.0;
        };
    }

    public @NotNull Double maxJumpHeight() {
        return switch (mountType) {
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE -> 5.29;
            case DONKEY -> 1.62;
            case LLAMA, TRADER_LLAMA -> 0.6;
            case CAMEL -> 0.0;
        };
    }

    public @NotNull Double minSpeed() {
        return switch (mountType) {
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE -> 4.74;
            case DONKEY -> 7.55;
            case LLAMA, TRADER_LLAMA -> 3.0;
            case CAMEL -> 3.88;
        };
    }

    public @NotNull Double maxSpeed() {
        return switch (mountType) {
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE -> 14.22;
            case DONKEY -> 7.55;
            case LLAMA, TRADER_LLAMA -> 4.0;
            case CAMEL -> 3.88;
        };
    }

    public @NotNull Integer minSlots() {
        return switch (mountType) {
            case DONKEY, MULE -> 15;
            case LLAMA, TRADER_LLAMA -> 3;
            default -> 0;
        };
    }

    public @NotNull Integer maxSlots() {
        return switch (mountType) {
            case DONKEY, MULE,  LLAMA, TRADER_LLAMA -> 15;
            case ZOMBIE_HORSE, SKELETON_HORSE, HORSE, CAMEL -> 0;
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
