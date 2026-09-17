package dev.gallon.fabric;

import dev.gallon.HorseStatsMod;
import dev.gallon.domain.ModConfig;
import dev.gallon.domain.ModMetadata;
import dev.gallon.fabric.config.ConfigScreenRegistry;
import dev.gallon.fabric.config.TheModConfig;
import dev.gallon.mixins.AbstractContainerScreenAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.equine.AbstractHorse;

public final class HorseStatsModFabric implements ClientModInitializer {
    private static final Identifier STATS_OVERLAY = Identifier.fromNamespaceAndPath(
            ModMetadata.MOD_ID,
            "stats_overlay"
    );

    private boolean pickBlockWasDown;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(TheModConfig.class, JanksonConfigSerializer::new);
        ConfigScreenRegistry.register();
        ModConfig config = AutoConfig.getConfigHolder(TheModConfig.class).get().modConfig;
        HorseStatsMod horseStatsMod = new HorseStatsMod(config);

        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof HorseInventoryScreen) {
                // Schedule tooltips before Screen extracts its deferred elements.
                ScreenEvents.afterForeground(screen).register((horseScreen, guiGraphics, mouseX, mouseY, tickDelta) -> {
                    guiGraphics.pose().pushMatrix();
                    try {
                        guiGraphics.pose().translate(
                                ((AbstractContainerScreenAccessor) horseScreen).getLeftPos(),
                                ((AbstractContainerScreenAccessor) horseScreen).getTopPos()
                        );

                        horseStatsMod.onRenderHorseContainerEvent(
                                (HorseInventoryScreen) horseScreen,
                                guiGraphics,
                                mouseX,
                                mouseY
                        );
                    } finally {
                        guiGraphics.pose().popMatrix();
                    }
                });
            }
        });

        HudElementRegistry.attachElementAfter(
                VanillaHudElements.OVERLAY_MESSAGE,
                STATS_OVERLAY,
                horseStatsMod::onRenderOverlayEvent
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            horseStatsMod.onClientTick();

            boolean pickBlockDown = client.options.keyPickItem.isDown();
            if (client.mouseHandler.isMouseGrabbed() && pickBlockDown && !pickBlockWasDown) {
                horseStatsMod.onMiddleClickEvent(client);
            }
            pickBlockWasDown = pickBlockDown;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClientSide()
                    && hand == InteractionHand.MAIN_HAND
                    && entity instanceof AbstractHorse horse) {

                horseStatsMod.onHorseInteractEvent(player, horse);
            }
            return InteractionResult.PASS;
        });
    }
}
