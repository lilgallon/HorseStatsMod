package dev.gallon;

import com.mojang.logging.LogUtils;
import dev.gallon.domain.AboveHeadKind;
import dev.gallon.domain.HorseStats;
import dev.gallon.domain.InteractionKind;
import dev.gallon.domain.ModConfig;
import dev.gallon.mixins.HorseInventoryScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

import static dev.gallon.services.DisplayService.buildAboveHeadLines;
import static dev.gallon.services.DisplayService.buildOverlayMessage;
import static dev.gallon.services.DisplayService.displayContainerStats;
import static dev.gallon.services.DisplayService.renderOverlayMessage;
import static dev.gallon.services.HorseStatsService.getHorseStats;

public final class HorseStatsMod {
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Lets the entity renderer mixins reach the mod, since they are not created by the loader entrypoints.
     */
    private static @Nullable HorseStatsMod instance;

    private final @NotNull ModConfig config;
    private final @NotNull OverlayState overlayState = new OverlayState();

    private boolean interactionDisplayEnabled = true;
    private boolean inventoryDisplayEnabled = true;
    private boolean overlayDisplayEnabled = true;
    private boolean aboveHeadDisplayEnabled = true;
    private @Nullable AbstractHorse lookedAtMount;

    public HorseStatsMod(@NotNull ModConfig config) {
        this.config = config;
        instance = this;
    }

    public static @NotNull Optional<HorseStatsMod> instance() {
        return Optional.ofNullable(instance);
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
        updateLookedAtMount();
    }

    /**
     * Vanilla's crosshair target only reaches entities within interaction range (3 blocks), which is too short to
     * pick a mount from a herd, so we cast our own ray once per tick.
     */
    private void updateLookedAtMount() {
        lookedAtMount = null;
        if (!aboveHeadDisplayEnabled || config.getDisplayStatsAboveHead() != AboveHeadKind.WHEN_LOOKING) {
            return;
        }

        try {
            Entity camera = Minecraft.getInstance().getCameraEntity();
            if (camera != null) {
                lookedAtMount = findLookedAtMount(camera, AboveHeadKind.WHEN_LOOKING_RANGE);
            }
        } catch (RuntimeException exception) {
            disableAboveHeadDisplay(exception);
        }
    }

    private static @Nullable AbstractHorse findLookedAtMount(@NotNull Entity camera, double range) {
        Vec3 from = camera.getEyePosition();
        HitResult blockHit = camera.pick(range, 1.0F, false);
        double maxDistanceSq = blockHit.getType() == HitResult.Type.MISS
                ? range * range
                : from.distanceToSqr(blockHit.getLocation());

        Vec3 view = camera.getViewVector(1.0F).scale(range);
        AABB searchArea = camera.getBoundingBox().expandTowards(view).inflate(1.0);
        Entity vehicle = camera.getVehicle();
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                camera,
                from,
                from.add(view),
                searchArea,
                entity -> entity instanceof AbstractHorse && entity != vehicle && entity.isPickable(),
                maxDistanceSq
        );

        return entityHit != null && entityHit.getEntity() instanceof AbstractHorse horse ? horse : null;
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

    /**
     * Called for every rendered entity on each frame.
     *
     * @return the lines to display above the entity's head, from top to bottom, or an empty list
     */
    public @NotNull List<Component> getAboveHeadLines(@NotNull Entity entity, double distanceToCameraSq) {
        if (!aboveHeadDisplayEnabled || !(entity instanceof AbstractHorse horse)) {
            return List.of();
        }

        try {
            Minecraft minecraft = Minecraft.getInstance();
            // Same visibility rules as vanilla name tags: hidden with the HUD, when invisible, or when ridden
            if (minecraft.player == null
                    || minecraft.gui.hud.isHidden()
                    || horse.isVehicle()
                    || horse.isInvisibleTo(minecraft.player)) {
                return List.of();
            }

            boolean lookedAt = horse == lookedAtMount;
            if (!config.getDisplayStatsAboveHead().shouldDisplay(lookedAt, distanceToCameraSq)) {
                return List.of();
            }

            return getHorseStats(horse, config.getIncludeAttributeModifiers())
                    .map(stats -> buildAboveHeadLines(config, stats))
                    .orElse(List.of());
        } catch (RuntimeException exception) {
            disableAboveHeadDisplay(exception);
            return List.of();
        }
    }

    private void disableAboveHeadDisplay(RuntimeException exception) {
        aboveHeadDisplayEnabled = false;
        lookedAtMount = null;
        LOGGER.error(
                "HorseStatsMod disabled the statistics above mounts for this session after a failure",
                exception
        );
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

}
