package dev.gallon.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModConfig {
    private static final Boolean DEFAULT_DISPLAY_STATS_IN_INVENTORY = true;
    private static final InteractionKind DEFAULT_DISPLAY_STATS_ON_INTERACTION = InteractionKind.RIGHT_CLICK;
    private static final Boolean DEFAULT_COLORED_STATS = true;
    private static final DisplayMinMax DEFAULT_DISPLAY_MIN_MAX = DisplayMinMax.DISABLED;
    private static final Boolean DEFAULT_DISPLAY_STATS_IN_PERCENTAGE = false;
    private static final GroupedKind DEFAULT_GROUPED_STATS = GroupedKind.INDIVIDUAL;
    private static final Boolean DEFAULT_INCLUDE_ATTRIBUTE_MODIFIERS = true;

    private Boolean displayStatsInInventory = DEFAULT_DISPLAY_STATS_IN_INVENTORY;
    private InteractionKind displayStatsOnInteraction = DEFAULT_DISPLAY_STATS_ON_INTERACTION;
    private Boolean coloredStats = DEFAULT_COLORED_STATS;
    private DisplayMinMax displayMinMax = DEFAULT_DISPLAY_MIN_MAX;
    private Boolean displayStatsInPercentage = DEFAULT_DISPLAY_STATS_IN_PERCENTAGE;
    private GroupedKind groupedStats = DEFAULT_GROUPED_STATS;
    private Boolean includeAttributeModifiers = DEFAULT_INCLUDE_ATTRIBUTE_MODIFIERS;

    public @NotNull Boolean getDisplayStatsInInventory() {
        return Objects.requireNonNullElse(displayStatsInInventory, DEFAULT_DISPLAY_STATS_IN_INVENTORY);
    }

    public void setDisplayStatsInInventory(@NotNull Boolean displayStatsInInventory) {
        this.displayStatsInInventory = Objects.requireNonNullElse(
                displayStatsInInventory,
                DEFAULT_DISPLAY_STATS_IN_INVENTORY
        );
    }

    public @NotNull Boolean getColoredStats() {
        return Objects.requireNonNullElse(coloredStats, DEFAULT_COLORED_STATS);
    }

    public void setColoredStats(@NotNull Boolean coloredStats) {
        this.coloredStats = Objects.requireNonNullElse(coloredStats, DEFAULT_COLORED_STATS);
    }

    public @NotNull DisplayMinMax getDisplayMinMax() {
        return Objects.requireNonNullElse(displayMinMax, DEFAULT_DISPLAY_MIN_MAX);
    }

    public void setDisplayMinMax(@NotNull DisplayMinMax displayMinMax) {
        this.displayMinMax = Objects.requireNonNullElse(displayMinMax, DEFAULT_DISPLAY_MIN_MAX);
    }

    public @NotNull Boolean getDisplayStatsInPercentage() {
        return Objects.requireNonNullElse(displayStatsInPercentage, DEFAULT_DISPLAY_STATS_IN_PERCENTAGE);
    }

    public void setDisplayStatsInPercentage(@NotNull Boolean displayStatsInPercentage) {
        this.displayStatsInPercentage = Objects.requireNonNullElse(
                displayStatsInPercentage,
                DEFAULT_DISPLAY_STATS_IN_PERCENTAGE
        );
    }

    public @NotNull InteractionKind getDisplayStatsOnInteraction() {
        return Objects.requireNonNullElse(displayStatsOnInteraction, DEFAULT_DISPLAY_STATS_ON_INTERACTION);
    }

    public void setDisplayStatsOnInteraction(@NotNull InteractionKind displayStatsOnInteraction) {
        this.displayStatsOnInteraction = Objects.requireNonNullElse(
                displayStatsOnInteraction,
                DEFAULT_DISPLAY_STATS_ON_INTERACTION
        );
    }
    
    public @NotNull GroupedKind getGroupedStats() {
        return Objects.requireNonNullElse(groupedStats, DEFAULT_GROUPED_STATS);
    }

    public void setGroupedStats(@NotNull GroupedKind groupedStats) {
        this.groupedStats = Objects.requireNonNullElse(groupedStats, DEFAULT_GROUPED_STATS);
    }

    public @NotNull Boolean getIncludeAttributeModifiers() {
        return Objects.requireNonNullElse(includeAttributeModifiers, DEFAULT_INCLUDE_ATTRIBUTE_MODIFIERS);
    }

    public void setIncludeAttributeModifiers(@NotNull Boolean includeAttributeModifiers) {
        this.includeAttributeModifiers = Objects.requireNonNullElse(
                includeAttributeModifiers,
                DEFAULT_INCLUDE_ATTRIBUTE_MODIFIERS
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
        setGroupedStats(groupedStats);
        setIncludeAttributeModifiers(includeAttributeModifiers);
    }
}
