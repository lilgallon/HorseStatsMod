package dev.gallon.neoforge;

import dev.gallon.HorseStatsMod;
import dev.gallon.domain.ModMetadata;
import dev.gallon.mixins.AbstractContainerScreenAccessor;
import dev.gallon.neoforge.config.TheModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

@Mod(value = ModMetadata.MOD_ID, dist = Dist.CLIENT)
public final class HorseStatsModNeoForge {
    private static final Identifier STATS_OVERLAY = Identifier.fromNamespaceAndPath(
            ModMetadata.MOD_ID,
            "stats_overlay"
    );

    private final @NotNull HorseStatsMod horseStatsMod;

    public HorseStatsModNeoForge(ModContainer container) {
        this.horseStatsMod = new HorseStatsMod(TheModConfig.config);
        NeoForge.EVENT_BUS.addListener(this::onEntityInteractEvent);
        NeoForge.EVENT_BUS.addListener(this::onRenderContainerScreenEvent);
        NeoForge.EVENT_BUS.addListener(this::onInteractionKeyMappingTriggered);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
        container.getEventBus().addListener(this::onRegisterGuiLayers);
        container.registerConfig(ModConfig.Type.CLIENT, TheModConfig.CLIENT_SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
                VanillaGuiLayers.OVERLAY_MESSAGE,
                STATS_OVERLAY,
                horseStatsMod::onRenderOverlayEvent
        );
    }

    private void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isPickBlock()) {
            horseStatsMod.onMiddleClickEvent(Minecraft.getInstance());
        }
    }

    private void onClientTick(ClientTickEvent.Post event) {
        horseStatsMod.onClientTick();
    }

    private void onRenderContainerScreenEvent(ScreenEvent.Render.Foreground event) {
        if (event.getScreen() instanceof HorseInventoryScreen horseInventoryScreen) {
            AbstractContainerScreenAccessor screenAccessor =
                    (AbstractContainerScreenAccessor) horseInventoryScreen;

            event.getGuiGraphics().pose().pushMatrix();
            try {
                event.getGuiGraphics().pose().translate(
                        screenAccessor.getLeftPos(),
                        screenAccessor.getTopPos()
                );

                horseStatsMod.onRenderHorseContainerEvent(
                        horseInventoryScreen,
                        event.getGuiGraphics(),
                        event.getMouseX(),
                        event.getMouseY()
                );
            } finally {
                event.getGuiGraphics().pose().popMatrix();
            }
        }
    }

    private void onEntityInteractEvent(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide()
                && event.getHand() == InteractionHand.MAIN_HAND
                && event.getTarget() instanceof AbstractHorse horse) {

            horseStatsMod.onHorseInteractEvent(event.getEntity(), horse);
        }
    }
}
