package dev.gallon.domain;

import org.jetbrains.annotations.NotNull;

public class ModConfig {
    private @NotNull Boolean displayStats = false;
    private @NotNull Boolean coloredStats = true;
    private @NotNull Boolean displayMinMax = false;

    public @NotNull Boolean getDisplayStats() {
        return displayStats;
    }

    public void setDisplayStats(@NotNull Boolean displayStats) {
        this.displayStats = displayStats;
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
}
