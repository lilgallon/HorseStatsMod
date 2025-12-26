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
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE, DONKEY -> 1.153;
            case LLAMA, TRADER_LLAMA, CAMEL -> 0.0;
        };
    }

    public @NotNull Double maxJumpHeight() {
        return switch (mountType) {
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE, DONKEY -> 5.9196;
            case LLAMA, TRADER_LLAMA, CAMEL -> 0.0;
        };
    }

    public @NotNull Double minSpeed() {
        return switch (mountType) {
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE, DONKEY -> 4.856;
            case LLAMA, TRADER_LLAMA -> 3.0;
            case CAMEL -> 3.88;
        };
    }

    public @NotNull Double maxSpeed() {
        return switch (mountType) {
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE, MULE, DONKEY -> 14.57;
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

    private @NotNull Double computePercentage(double value, double min, double max) {
        if (max == min) {
            return max == 0 ? 0.0 : 100.0;
        } else {
            return ((value - min) / (max - min) * 100.0);
        }
    }

    private @NotNull Integer getHealthPercentage() {
        return (int) Math.round(computePercentage(health, minHealth(), maxHealth()));
    }

    private @NotNull Double getJumpHeightPercentage() {
        return computePercentage(jumpHeight, minJumpHeight(), maxJumpHeight());
    }

    private @NotNull Double getSpeedPercentage() {
        return computePercentage(speed, minSpeed(), maxSpeed());
    }

    private @NotNull Integer getSlotsPercentage() {
        return (int) Math.round(computePercentage(slots.orElse(minSlots()), minSlots(), maxSlots()));
    }

    public @NotNull String getHealthStr(Boolean percentage) {
        return percentage ? (getHealthPercentage() + "%" ) : String.format("%.2f", health);
    }

    public @NotNull String getJumpHeightStr(Boolean percentage) {
        return percentage ? String.format("%.3f%%", getJumpHeightPercentage()) : String.format("%.2f", jumpHeight);
    }

    public @NotNull String getSpeedStr(Boolean percentage) {
        return percentage ? String.format("%.3f%%", getSpeedPercentage()) : String.format("%.2f", speed);
    }

    public @NotNull String getSlotsStr(Boolean percentage) {
        return percentage ? (getSlotsPercentage() + "%" ) : slots.orElse(0).toString();
    }

    public double getGroupedStats() {
        double rawValue = computePercentage(
            health + speed + jumpHeight,
            (double) minHealth() + minSpeed() + minJumpHeight(),
            (double) maxHealth() + maxSpeed() + maxJumpHeight()
        );
        return Math.round(rawValue * 1000.0) / 1000.0;
    }

    public @NotNull String getGroupedStatsStr() {
        return String.format("%.3f%%", getGroupedStats());
    }
}