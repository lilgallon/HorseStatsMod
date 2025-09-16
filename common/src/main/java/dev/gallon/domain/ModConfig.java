package dev.gallon.domain;

import org.jetbrains.annotations.NotNull;

public class ModConfig {
    private @NotNull Boolean displayStatsInInventory = true;
    private @NotNull Boolean displayStatsOnRightClick = true;
    private @NotNull Boolean coloredStats = true;
    private @NotNull Boolean displayMinMax = false;
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

    public @NotNull Boolean getDisplayMinMax() {
        return displayMinMax;
    }

    public void setDisplayMinMax(@NotNull Boolean displayMinMax) {
        this.displayMinMax = displayMinMax;
    }

    public @NotNull Boolean getStatsInPercentage() {
        return statsInPercentage;
    }

    public void setStatsInPercentage(@NotNull Boolean statsInPercentage) {
        this.statsInPercentage = statsInPercentage;
    }

    public @NotNull Boolean getDisplayStatsOnRightClick() {
        return displayStatsOnRightClick;
    }

    public void setDisplayStatsOnRightClick(@NotNull Boolean displayStatsOnRightClick) {
        this.displayStatsOnRightClick = displayStatsOnRightClick;
    }
}
