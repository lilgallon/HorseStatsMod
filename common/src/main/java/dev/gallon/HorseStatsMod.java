package dev.gallon;

import dev.gallon.domain.HorseStats;
import dev.gallon.domain.InteractionKind;
import dev.gallon.domain.ModConfig;
import dev.gallon.mixins.HorseInventoryScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
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

    public void onHorseInteractEvent(@NotNull Player interactor, @NotNull AbstractHorse horse) {
        if (!interactor.equals(Minecraft.getInstance().player)) {
            return;
        }

        boolean rightOrShiftClickConfigured = config.getDisplayStatsOnInteraction() == InteractionKind.RIGHT_OR_SHIFT_RIGHT_CLICK;
        boolean shiftRightClickConfiguredAndDown = config.getDisplayStatsOnInteraction() == InteractionKind.SHIFT_RIGHT_CLICK &&
                (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isShiftKeyDown());
        boolean rightClickConfiguredAndShiftNotDown = config.getDisplayStatsOnInteraction() == InteractionKind.RIGHT_CLICK  &&
                (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.isShiftKeyDown());

        if (rightOrShiftClickConfigured || shiftRightClickConfiguredAndDown || rightClickConfiguredAndShiftNotDown) {
            horseStats = getHorseStats(horse);
            horseStats.ifPresent(stats -> displayOverlayStats(config, stats));
        }
    }

    public void onRenderHorseContainerEvent(
            HorseInventoryScreen horseInventoryScreen,
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY
    ) {
        HorseInventoryScreenAccessor horseInventoryScreenAccessor = (HorseInventoryScreenAccessor) horseInventoryScreen;

        LivingEntity mount = horseInventoryScreenAccessor.getMount();
        if (!(mount instanceof AbstractHorse)) {
            return;
        }

        Optional<HorseStats> containerHorseStats = getHorseStats((AbstractHorse) horseInventoryScreenAccessor.getMount());

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
