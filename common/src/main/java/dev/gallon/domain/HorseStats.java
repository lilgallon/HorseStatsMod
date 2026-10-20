package dev.gallon.domain;

import dev.gallon.services.HorseStatsService;
import dev.gallon.services.JumpHeightConverter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            // AbstractHorse#generateMaxHealth: 15 + random(0..7) + random(0..8), used by horses, donkeys, mules and
            // llamas. Bred mounts keep the same 15 - 30 range.
            case HORSE, DONKEY, MULE, LLAMA, TRADER_LLAMA -> 15;

            // SkeletonHorse#createAttributes: always 15
            case SKELETON_HORSE -> 15;

            // ZombieHorse#createAttributes: always 25
            case ZOMBIE_HORSE -> 25;

            // Camel#createAttributes: always 32
            case CAMEL -> 32;
        };
    }

    public @NotNull Integer maxHealth() {
        return switch (mountType) {
            case HORSE, DONKEY, MULE, LLAMA, TRADER_LLAMA -> 30;
            case SKELETON_HORSE -> 15;
            case ZOMBIE_HORSE -> 25;
            case CAMEL -> 32;
        };
    }

    public @NotNull Double minJumpHeight() {
        return JumpHeightConverter.getJumpHeight(minJumpStrength());
    }

    public @NotNull Double maxJumpHeight() {
        return JumpHeightConverter.getJumpHeight(maxJumpStrength());
    }

    private double minJumpStrength() {
        return switch (mountType) {
            // AbstractHorse#generateJumpStrength: 0.4F + 3 * random(0..0.2). Spawned donkeys and mules always have 0.5,
            // but bred ones have the same 0.4 - 1.0 range as horses. Skeleton horses are randomized like horses.
            case HORSE, DONKEY, MULE, SKELETON_HORSE -> 0.4F;

            // ZombieHorse#generateZombieHorseJumpStrength: 0.5 + 3 * random(0..1/15)
            case ZOMBIE_HORSE -> 0.5;

            // AbstractChestedHorse#createBaseChestedHorseAttributes: always 0.5, llamas can't jump with a rider though
            case LLAMA, TRADER_LLAMA -> 0.5;

            // Camel#createAttributes: always 0.42F, camels dash instead of jumping
            case CAMEL -> 0.42F;
        };
    }

    private double maxJumpStrength() {
        return switch (mountType) {
            case HORSE, DONKEY, MULE, SKELETON_HORSE -> 0.4F + 0.2 + 0.2 + 0.2;
            case ZOMBIE_HORSE -> 0.5 + 0.06666666666666667 + 0.06666666666666667 + 0.06666666666666667;
            case LLAMA, TRADER_LLAMA -> 0.5;
            case CAMEL -> 0.42F;
        };
    }

    public @NotNull Double minSpeed() {
        return HorseStatsService.convertSpeedToBlocksPerSeconds(minSpeedAttribute());
    }

    public @NotNull Double maxSpeed() {
        return HorseStatsService.convertSpeedToBlocksPerSeconds(maxSpeedAttribute());
    }

    private double minSpeedAttribute() {
        return switch (mountType) {
            // AbstractHorse#generateSpeed: (0.45F + 3 * random(0..0.3)) * 0.25. Spawned donkeys and mules always have
            // 0.175, but bred ones have the same 0.1125 - 0.3375 range as horses.
            case HORSE, DONKEY, MULE -> 0.45F * 0.25;

            // SkeletonHorse#createAttributes: always 0.2F
            case SKELETON_HORSE -> 0.2F;

            // ZombieHorse#generateZombieHorseSpeed: (9 + 3 * random(0..1)) / 42.16F
            case ZOMBIE_HORSE -> 9.0 / 42.16F;

            // AbstractChestedHorse#createBaseChestedHorseAttributes: always 0.175F
            case LLAMA, TRADER_LLAMA -> 0.175F;

            // Camel#createAttributes: always 0.09F (walking speed, camels can also dash and sprint)
            case CAMEL -> 0.09F;
        };
    }

    private double maxSpeedAttribute() {
        return switch (mountType) {
            case HORSE, DONKEY, MULE -> (0.45F + 0.3 + 0.3 + 0.3) * 0.25;
            case SKELETON_HORSE -> 0.2F;
            case ZOMBIE_HORSE -> (9.0 + 1.0 + 1.0 + 1.0) / 42.16F;
            case LLAMA, TRADER_LLAMA -> 0.175F;
            case CAMEL -> 0.09F;
        };
    }

    public @NotNull Integer minSlots() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Donkey#Equipment: An additional 15 inventory slots, provided the donkey has been
            // equipped with a chest.
            case DONKEY -> 15;

            // https://minecraft.wiki/w/Mule#Equipment: A mule starts out with just a saddle slot, but if it is given a
            // chest, it acquires 15 more inventory slots that can hold any items.
            case MULE -> 15;

            // https://minecraft.wiki/w/Llama#Storage: A tamed llama can be equipped with a chest by pressing the use
            // control on it while holding a chest. The chest gives the llama 3 to 15 slots of inventory space,
            // depending on its strength
            case LLAMA -> 3;

            // https://minecraft.wiki/w/Trader_Llama#Storage: A tamed trader llama can be equipped with a chest by
            // pressing the use control on it while holding a chest. The chest gives the trader llama 3 to 15 slots of
            // inventory space, depending on its strength
            case TRADER_LLAMA -> 3;

            // No mention of slots in wiki
            case ZOMBIE_HORSE, SKELETON_HORSE, HORSE, CAMEL -> 0;
        };
    }

    public @NotNull Integer maxSlots() {
        return switch (mountType) {
            // https://minecraft.wiki/w/Donkey#Equipment: An additional 15 inventory slots, provided the donkey has been
            // equipped with a chest.
            case DONKEY, MULE -> 15;

            // https://minecraft.wiki/w/Llama#Storage: A tamed llama can be equipped with a chest by pressing the use
            // control on it while holding a chest. The chest gives the llama 3 to 15 slots of inventory space,
            // depending on its strength
            case LLAMA -> 15;

            // https://minecraft.wiki/w/Trader_Llama#Storage: A tamed trader llama can be equipped with a chest by
            // pressing the use control on it while holding a chest. The chest gives the trader llama 3 to 15 slots of
            // inventory space, depending on its strength
            case TRADER_LLAMA -> 15;

            // No mention of slots in wiki
            case ZOMBIE_HORSE, SKELETON_HORSE, HORSE, CAMEL -> 0;
        };
    }

    private double normalize(double value, double min, double max) {
        if (max == min) {
            return max == 0 ? 0 : 1;
        } else {
            return (value - min) / (max - min);
        }
    }

    public double getHealthPercentage() {
        return normalize(health, minHealth(), maxHealth()) * 100;
    }

    public double getJumpHeightPercentage() {
        return normalize(jumpHeight, minJumpHeight(), maxJumpHeight()) * 100;
    }

    public double getSpeedPercentage() {
        return normalize(speed, minSpeed(), maxSpeed()) * 100;
    }

    public double getSlotsPercentage() {
        return normalize(slots.orElse(minSlots()), minSlots(), maxSlots()) * 100;
    }

    public @NotNull String getHealthStr(@NotNull ModConfig config) {
        return config.getDisplayStatsInPercentage()
                ? formatPercentage(getHealthPercentage(), config)
                : formatHealth(health, config);
    }

    public @NotNull String getJumpHeightStr(@NotNull ModConfig config) {
        return config.getDisplayStatsInPercentage()
                ? formatPercentage(getJumpHeightPercentage(), config)
                : formatJumpHeight(jumpHeight, config);
    }

    public @NotNull String getSpeedStr(@NotNull ModConfig config) {
        return config.getDisplayStatsInPercentage()
                ? formatPercentage(getSpeedPercentage(), config)
                : formatSpeed(speed, config);
    }

    public @NotNull String getSlotsStr(@NotNull ModConfig config) {
        return config.getDisplayStatsInPercentage()
                ? formatPercentage(getSlotsPercentage(), config)
                : slots.orElse(0).toString();
    }

    /**
     * @return the average of the health, jump height and speed percentages (0 - 100)
     */
    public double getGroupedStats() {
        return (getHealthPercentage() + getJumpHeightPercentage() + getSpeedPercentage()) / 3;
    }

    public @NotNull String getGroupedStatsStr(@NotNull ModConfig config) {
        return formatPercentage(getGroupedStats(), config);
    }

    public static @NotNull String formatHealth(double health, @NotNull ModConfig config) {
        return formatDecimals(health, config.getHealthDecimals());
    }

    public static @NotNull String formatJumpHeight(double jumpHeight, @NotNull ModConfig config) {
        return formatDecimals(jumpHeight, config.getJumpHeightDecimals());
    }

    public static @NotNull String formatSpeed(double speed, @NotNull ModConfig config) {
        return formatDecimals(speed, config.getSpeedDecimals());
    }

    public static @NotNull String formatPercentage(double percentage, @NotNull ModConfig config) {
        return formatDecimals(percentage, config.getPercentageWithDecimal() ? 1 : 0) + "%";
    }

    /**
     * Rounds half up (65.5 -> 66, 65.45 -> 65.5 with one decimal) and formats with the user's locale.
     */
    static @NotNull String formatDecimals(double value, int decimals) {
        if (!Double.isFinite(value)) {
            return String.valueOf(value);
        }

        // BigDecimal.valueOf rounds from the shortest decimal representation of the double, so 65.45 stays 65.45
        // instead of 65.4499999... A BigDecimal is never negative zero, so -0.001 is displayed as 0 and not -0.
        BigDecimal rounded = BigDecimal.valueOf(value).setScale(decimals, RoundingMode.HALF_UP);
        return String.format("%." + decimals + "f", rounded);
    }
}
