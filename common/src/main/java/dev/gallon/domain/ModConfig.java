package dev.gallon.domain;

import org.jetbrains.annotations.NotNull;

public class ModConfig {
    private @NotNull Boolean displayStatsInInventory = true;
    private @NotNull InteractionKind displayStatsOnInteraction = InteractionKind.RIGHT_CLICK;
    private @NotNull Boolean coloredStats = true;
    private @NotNull DisplayMinMax displayMinMax = DisplayMinMax.DISABLED;
    private @NotNull Boolean statsInPercentage = true;

    public @NotNull Boolean getDisplayStatsInInventory() {
        return displayStatsInInventory;
    }

    public void setDisplayStatsInInventory(@NotNull Boolean displayStatsInInventory) {
        this.displayStatsInInventory = displayStatsInInventory;
    }

    public @NotNull Boolean getColoredStats() {
        return coloredStats;
    }

    public void setColoredStats(@NotNull Boolean coloredStats) {
        this.coloredStats = coloredStats;
    }

    public @NotNull DisplayMinMax getDisplayMinMax() {
        return displayMinMax;
    }

    public void setDisplayMinMax(@NotNull DisplayMinMax displayMinMax) {
        this.displayMinMax = displayMinMax;
    }

    public @NotNull Boolean getStatsInPercentage() {
        return statsInPercentage;
    }

    public void setStatsInPercentage(@NotNull Boolean statsInPercentage) {
        this.statsInPercentage = statsInPercentage;
    }

    public @NotNull InteractionKind getDisplayStatsOnInteraction() {
        return displayStatsOnInteraction;
    }

    public void setDisplayStatsOnInteraction(@NotNull InteractionKind displayStatsOnInteraction) {
        this.displayStatsOnInteraction = displayStatsOnInteraction;
    }
}
