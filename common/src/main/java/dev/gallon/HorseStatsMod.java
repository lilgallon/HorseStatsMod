package dev.gallon;

import dev.gallon.domain.HorseStats;
import dev.gallon.domain.InteractionKind;
import dev.gallon.domain.ModConfig;
import dev.gallon.mixins.HorseInventoryScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static dev.gallon.services.DisplayService.displayContainerStats;
import static dev.gallon.services.DisplayService.displayOverlayStats;
import static dev.gallon.services.HorseStatsService.getHorseStats;

public final class HorseStatsMod {
    private static @Nullable HorseStatsMod instance;

    private final @NotNull ModConfig config;
    private @NotNull Optional<HorseStats> horseStats;

    public HorseStatsMod(@NotNull ModConfig config) {
        this.config = config;
        this.horseStats = Optional.empty();
        instance = this;
    }

    public void onHorseInteractEvent(@NotNull Player interactor, @NotNull AbstractHorse horse) {
        if (!interactor.equals(Minecraft.getInstance().player)) {
            return;
        }

        boolean shiftKeyDown = Minecraft.getInstance().player != null && Minecraft.getInstance().player.isShiftKeyDown();

        if (config.getDisplayStatsOnInteraction().matchesRightClick(shiftKeyDown)) {
            displayHorseStats(horse);
        }
    }

    public static void onMiddleClickEvent(@NotNull Minecraft minecraft) {
        if (instance == null || instance.config.getDisplayStatsOnInteraction() != InteractionKind.MIDDLE_CLICK) {
            return;
        }

        if (minecraft.player != null
                && minecraft.hitResult instanceof EntityHitResult entityHitResult
                && entityHitResult.getEntity() instanceof AbstractHorse horse) {
            instance.displayHorseStats(horse);
        }
    }

    private void displayHorseStats(@NotNull AbstractHorse horse) {
        horseStats = getHorseStats(horse, config.getIncludeAttributeModifiers());
        horseStats.ifPresent(stats -> displayOverlayStats(config, stats));
    }

    public void onRenderHorseContainerEvent(
            HorseInventoryScreen horseInventoryScreen,
            GuiGraphicsExtractor guiGraphics,
            int mouseX,
            int mouseY
    ) {
        HorseInventoryScreenAccessor horseInventoryScreenAccessor = (HorseInventoryScreenAccessor) horseInventoryScreen;

        LivingEntity mount = horseInventoryScreenAccessor.getMount();
        if (!(mount instanceof AbstractHorse)) {
            return;
        }

        Optional<HorseStats> containerHorseStats = getHorseStats(
                (AbstractHorse) horseInventoryScreenAccessor.getMount(),
                config.getIncludeAttributeModifiers()
        );

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
