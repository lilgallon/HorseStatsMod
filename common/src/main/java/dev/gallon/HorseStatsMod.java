package dev.gallon;

import dev.gallon.domain.HorseStats;
import dev.gallon.domain.ModConfig;
import dev.gallon.mixins.HorseInventoryScreenAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static dev.gallon.services.DisplayService.displayContainerStats;
import static dev.gallon.services.DisplayService.displayOverlayStats;
import static dev.gallon.services.HorseStatsService.getHorseStats;

public final class HorseStatsMod {
    private final @NotNull ModConfig config;
    private @NotNull Optional<HorseStats> horseStats;

    public HorseStatsMod(@NotNull ModConfig config) {
        this.config = config;
        this.horseStats = Optional.empty();
    }

    public void onHorseInteractEvent(@NotNull AbstractHorse horse) {
        horseStats = getHorseStats(horse);
        horseStats.ifPresent(stats -> displayOverlayStats(config, stats));
    }

    public void onRenderHorseContainerEvent(
            HorseInventoryScreen horseInventoryScreen,
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY
    ) {
        HorseInventoryScreenAccessor horseInventoryScreenAccessor = (HorseInventoryScreenAccessor) horseInventoryScreen;
        Optional<HorseStats> containerHorseStats = getHorseStats(horseInventoryScreenAccessor.getHorse());

        if (containerHorseStats.isPresent()) {
            int relativeMouseX = (mouseX - horseInventoryScreenAccessor.getLeftPos());
            int relativeMouseY = (mouseY - horseInventoryScreenAccessor.getTopPos());

            displayContainerStats(
                    guiGraphics,
                    config,
                    containerHorseStats.get(),
                    - (horseInventoryScreenAccessor.getLeftPos() * 2 - horseInventoryScreen.width),
                    relativeMouseX,
                    relativeMouseY
            );

        }
    }
}
