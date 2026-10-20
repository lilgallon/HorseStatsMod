package dev.gallon.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModConfig {
    /**
     * Kept out of the instance's fields: config GUI libraries (AutoConfig) enumerate every declared
     * field of {@link ModConfig}, including statics, and fail on save when they try to write them.
     */
    private static final class Defaults {
        static final Boolean DISPLAY_STATS_IN_INVENTORY = true;
        static final InteractionKind DISPLAY_STATS_ON_INTERACTION = InteractionKind.RIGHT_CLICK;
        static final Boolean COLORED_STATS = true;
        static final DisplayMinMax DISPLAY_MIN_MAX = DisplayMinMax.DISABLED;
        static final Boolean DISPLAY_STATS_IN_PERCENTAGE = false;
        static final Boolean PERCENTAGE_WITH_DECIMAL = false;
        static final Integer HEALTH_DECIMALS = 0;
        static final Integer JUMP_HEIGHT_DECIMALS = 2;
        static final Integer SPEED_DECIMALS = 2;
        static final GroupedKind GROUPED_STATS = GroupedKind.INDIVIDUAL;
        static final Boolean INCLUDE_ATTRIBUTE_MODIFIERS = true;
        static final AboveHeadKind DISPLAY_STATS_ABOVE_HEAD = AboveHeadKind.DISABLED;
    }

    /**
     * Bounds of the number of decimals the user can choose for each stat. Kept out of the instance's fields for the
     * same reason as {@link Defaults}.
     */
    public static final class Decimals {
        public static final int MIN = 0;
        public static final int MAX = 5;

        private Decimals() {
        }
    }

    private Boolean displayStatsInInventory = Defaults.DISPLAY_STATS_IN_INVENTORY;
    private InteractionKind displayStatsOnInteraction = Defaults.DISPLAY_STATS_ON_INTERACTION;
    private Boolean coloredStats = Defaults.COLORED_STATS;
    private DisplayMinMax displayMinMax = Defaults.DISPLAY_MIN_MAX;
    private Boolean displayStatsInPercentage = Defaults.DISPLAY_STATS_IN_PERCENTAGE;
    private Boolean percentageWithDecimal = Defaults.PERCENTAGE_WITH_DECIMAL;
    private Integer healthDecimals = Defaults.HEALTH_DECIMALS;
    private Integer jumpHeightDecimals = Defaults.JUMP_HEIGHT_DECIMALS;
    private Integer speedDecimals = Defaults.SPEED_DECIMALS;
    private GroupedKind groupedStats = Defaults.GROUPED_STATS;
    private Boolean includeAttributeModifiers = Defaults.INCLUDE_ATTRIBUTE_MODIFIERS;
    private AboveHeadKind displayStatsAboveHead = Defaults.DISPLAY_STATS_ABOVE_HEAD;

    public @NotNull Boolean getDisplayStatsInInventory() {
        return Objects.requireNonNullElse(displayStatsInInventory, Defaults.DISPLAY_STATS_IN_INVENTORY);
    }

    public void setDisplayStatsInInventory(@NotNull Boolean displayStatsInInventory) {
        this.displayStatsInInventory = Objects.requireNonNullElse(
                displayStatsInInventory,
                Defaults.DISPLAY_STATS_IN_INVENTORY
        );
    }

    public @NotNull Boolean getColoredStats() {
        return Objects.requireNonNullElse(coloredStats, Defaults.COLORED_STATS);
    }

    public void setColoredStats(@NotNull Boolean coloredStats) {
        this.coloredStats = Objects.requireNonNullElse(coloredStats, Defaults.COLORED_STATS);
    }

    public @NotNull DisplayMinMax getDisplayMinMax() {
        return Objects.requireNonNullElse(displayMinMax, Defaults.DISPLAY_MIN_MAX);
    }

    public void setDisplayMinMax(@NotNull DisplayMinMax displayMinMax) {
        this.displayMinMax = Objects.requireNonNullElse(displayMinMax, Defaults.DISPLAY_MIN_MAX);
    }

    public @NotNull Boolean getDisplayStatsInPercentage() {
        return Objects.requireNonNullElse(displayStatsInPercentage, Defaults.DISPLAY_STATS_IN_PERCENTAGE);
    }

    public void setDisplayStatsInPercentage(@NotNull Boolean displayStatsInPercentage) {
        this.displayStatsInPercentage = Objects.requireNonNullElse(
                displayStatsInPercentage,
                Defaults.DISPLAY_STATS_IN_PERCENTAGE
        );
    }

    public @NotNull Boolean getPercentageWithDecimal() {
        return Objects.requireNonNullElse(percentageWithDecimal, Defaults.PERCENTAGE_WITH_DECIMAL);
    }

    public void setPercentageWithDecimal(@NotNull Boolean percentageWithDecimal) {
        this.percentageWithDecimal = Objects.requireNonNullElse(percentageWithDecimal, Defaults.PERCENTAGE_WITH_DECIMAL);
    }

    public @NotNull Integer getHealthDecimals() {
        return validDecimals(healthDecimals, Defaults.HEALTH_DECIMALS);
    }

    public void setHealthDecimals(@NotNull Integer healthDecimals) {
        this.healthDecimals = validDecimals(healthDecimals, Defaults.HEALTH_DECIMALS);
    }

    public @NotNull Integer getJumpHeightDecimals() {
        return validDecimals(jumpHeightDecimals, Defaults.JUMP_HEIGHT_DECIMALS);
    }

    public void setJumpHeightDecimals(@NotNull Integer jumpHeightDecimals) {
        this.jumpHeightDecimals = validDecimals(jumpHeightDecimals, Defaults.JUMP_HEIGHT_DECIMALS);
    }

    public @NotNull Integer getSpeedDecimals() {
        return validDecimals(speedDecimals, Defaults.SPEED_DECIMALS);
    }

    public void setSpeedDecimals(@NotNull Integer speedDecimals) {
        this.speedDecimals = validDecimals(speedDecimals, Defaults.SPEED_DECIMALS);
    }

    /**
     * @return the given number of decimals clamped between {@link Decimals#MIN} and {@link Decimals#MAX}, or the
     * default value if there is none
     */
    private static @NotNull Integer validDecimals(Integer decimals, @NotNull Integer defaultValue) {
        if (decimals == null) {
            return defaultValue;
        }
        return Math.clamp(decimals, Decimals.MIN, Decimals.MAX);
    }

    public @NotNull InteractionKind getDisplayStatsOnInteraction() {
        return Objects.requireNonNullElse(displayStatsOnInteraction, Defaults.DISPLAY_STATS_ON_INTERACTION);
    }

    public void setDisplayStatsOnInteraction(@NotNull InteractionKind displayStatsOnInteraction) {
        this.displayStatsOnInteraction = Objects.requireNonNullElse(
                displayStatsOnInteraction,
                Defaults.DISPLAY_STATS_ON_INTERACTION
        );
    }
    
    public @NotNull GroupedKind getGroupedStats() {
        return Objects.requireNonNullElse(groupedStats, Defaults.GROUPED_STATS);
    }

    public void setGroupedStats(@NotNull GroupedKind groupedStats) {
        this.groupedStats = Objects.requireNonNullElse(groupedStats, Defaults.GROUPED_STATS);
    }

    public @NotNull Boolean getIncludeAttributeModifiers() {
        return Objects.requireNonNullElse(includeAttributeModifiers, Defaults.INCLUDE_ATTRIBUTE_MODIFIERS);
    }

    public void setIncludeAttributeModifiers(@NotNull Boolean includeAttributeModifiers) {
        this.includeAttributeModifiers = Objects.requireNonNullElse(
                includeAttributeModifiers,
                Defaults.INCLUDE_ATTRIBUTE_MODIFIERS
        );
    }

    public @NotNull AboveHeadKind getDisplayStatsAboveHead() {
        return Objects.requireNonNullElse(displayStatsAboveHead, Defaults.DISPLAY_STATS_ABOVE_HEAD);
    }

    public void setDisplayStatsAboveHead(@NotNull AboveHeadKind displayStatsAboveHead) {
        this.displayStatsAboveHead = Objects.requireNonNullElse(
                displayStatsAboveHead,
                Defaults.DISPLAY_STATS_ABOVE_HEAD
        );
    }

    /**
     * Replaces entries that a config serializer could not read with their defaults.
     */
    public void resetInvalidValues() {
        setDisplayStatsInInventory(displayStatsInInventory);
        setDisplayStatsOnInteraction(displayStatsOnInteraction);
        setColoredStats(coloredStats);
        setDisplayMinMax(displayMinMax);
        setDisplayStatsInPercentage(displayStatsInPercentage);
        setPercentageWithDecimal(percentageWithDecimal);
        setHealthDecimals(healthDecimals);
        setJumpHeightDecimals(jumpHeightDecimals);
        setSpeedDecimals(speedDecimals);
        setGroupedStats(groupedStats);
        setIncludeAttributeModifiers(includeAttributeModifiers);
        setDisplayStatsAboveHead(displayStatsAboveHead);
    }
}
