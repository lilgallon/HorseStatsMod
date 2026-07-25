package dev.gallon;

import com.mojang.logging.LogUtils;
import dev.gallon.domain.HorseStats;
import dev.gallon.domain.InteractionKind;
import dev.gallon.domain.ModConfig;
import dev.gallon.mixins.HudAccessor;
import dev.gallon.mixins.HorseInventoryScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;

import static dev.gallon.services.DisplayService.buildOverlayMessage;
import static dev.gallon.services.DisplayService.displayContainerStats;
import static dev.gallon.services.DisplayService.renderOverlayMessage;
import static dev.gallon.services.HorseStatsService.getHorseStats;

public final class HorseStatsMod {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final @NotNull ModConfig config;
    private final @NotNull OverlayState overlayState = new OverlayState();
    private final @NotNull MountHintState mountHintState = new MountHintState();

    private boolean interactionDisplayEnabled = true;
    private boolean inventoryDisplayEnabled = true;
    private boolean overlayDisplayEnabled = true;
    private boolean mountHintSuppressionEnabled = true;

    public HorseStatsMod(@NotNull ModConfig config) {
        this.config = config;
    }

    public void onHorseInteractEvent(@NotNull Player interactor, @NotNull AbstractHorse horse) {
        if (!interactionDisplayEnabled) {
            return;
        }

        try {
            Minecraft minecraft = Minecraft.getInstance();
            if (!interactor.equals(minecraft.player)) {
                return;
            }

            boolean shiftKeyDown = minecraft.player != null && minecraft.player.isShiftKeyDown();

            if (config.getDisplayStatsOnInteraction().matchesRightClick(shiftKeyDown)) {
                displayHorseStats(horse);
            }
        } catch (RuntimeException exception) {
            disableInteractionDisplay("right-click interaction", exception);
        }
    }

    public void onMiddleClickEvent(@NotNull Minecraft minecraft) {
        if (!interactionDisplayEnabled
                || config.getDisplayStatsOnInteraction() != InteractionKind.MIDDLE_CLICK) {
            return;
        }

        try {
            if (minecraft.player != null
                    && minecraft.hitResult instanceof EntityHitResult entityHitResult
                    && entityHitResult.getEntity() instanceof AbstractHorse horse) {
                displayHorseStats(horse);
            }
        } catch (RuntimeException exception) {
            disableInteractionDisplay("pick-block interaction", exception);
        }
    }

    private void displayHorseStats(@NotNull AbstractHorse horse) {
        getHorseStats(horse, config.getIncludeAttributeModifiers())
                .ifPresent(stats -> overlayState.show(buildOverlayMessage(config, stats)));
    }

    public void onClientTick() {
        overlayState.tick();
        suppressVanillaMountHint();
    }

    public void onRenderOverlayEvent(
            @NotNull GuiGraphicsExtractor guiGraphics,
            @NotNull DeltaTracker deltaTracker
    ) {
        if (!overlayDisplayEnabled) {
            return;
        }

        try {
            overlayState.message().ifPresent(message -> renderOverlayMessage(
                    guiGraphics,
                    message,
                    overlayState.remainingTicks(),
                    deltaTracker
            ));
        } catch (RuntimeException exception) {
            overlayDisplayEnabled = false;
            overlayState.clear();
            LOGGER.error("HorseStatsMod disabled its HUD overlay for this session after a rendering failure", exception);
        }
    }

    public void onRenderHorseContainerEvent(
            HorseInventoryScreen horseInventoryScreen,
            GuiGraphicsExtractor guiGraphics,
            int mouseX,
            int mouseY
    ) {
        if (!inventoryDisplayEnabled) {
            return;
        }

        try {
            HorseInventoryScreenAccessor horseInventoryScreenAccessor =
                    (HorseInventoryScreenAccessor) horseInventoryScreen;

            LivingEntity mount = horseInventoryScreenAccessor.getMount();
            if (!(mount instanceof AbstractHorse horse)) {
                return;
            }

            Optional<HorseStats> containerHorseStats = getHorseStats(
                    horse,
                    config.getIncludeAttributeModifiers()
            );

            if (containerHorseStats.isPresent()) {
                int relativeMouseX = mouseX - horseInventoryScreenAccessor.getLeftPos();
                int relativeMouseY = mouseY - horseInventoryScreenAccessor.getTopPos();

                displayContainerStats(
                        guiGraphics,
                        config,
                        containerHorseStats.get(),
                        -(horseInventoryScreenAccessor.getLeftPos() * 2 - horseInventoryScreen.width),
                        relativeMouseX,
                        relativeMouseY
                );
            }
        } catch (RuntimeException exception) {
            inventoryDisplayEnabled = false;
            LOGGER.error(
                    "HorseStatsMod disabled horse inventory rendering for this session after a rendering failure",
                    exception
            );
        }
    }

    private void disableInteractionDisplay(String source, RuntimeException exception) {
        interactionDisplayEnabled = false;
        overlayState.clear();
        LOGGER.error(
                "HorseStatsMod disabled interaction-triggered statistics for this session after a failure during {}",
                source,
                exception
        );
    }

    private void suppressVanillaMountHint() {
        if (!mountHintSuppressionEnabled) {
            return;
        }

        try {
            Minecraft minecraft = Minecraft.getInstance();
            var player = minecraft.player;
            var vehicle = player == null ? null : player.getVehicle();
            boolean isHorse = vehicle instanceof AbstractHorse;

            if (mountHintState.update(vehicle == null ? null : vehicle.getUUID(), isHorse)) {
                ((HudAccessor) minecraft.gui.hud).setOverlayMessageTime(0);
            }
        } catch (RuntimeException exception) {
            mountHintSuppressionEnabled = false;
            LOGGER.error(
                    "HorseStatsMod disabled vanilla horse mount hint suppression for this session after a failure",
                    exception
            );
        }
    }
}
