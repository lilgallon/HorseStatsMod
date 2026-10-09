package dev.gallon.forge;

import dev.gallon.HorseStatsMod;
import dev.gallon.domain.ModMetadata;
import dev.gallon.forge.config.HorseStatsConfigScreen;
import dev.gallon.forge.config.TheModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(ModMetadata.MOD_ID)
public final class HorseStatsModForge {
    private static final Identifier STATS_OVERLAY = Identifier.fromNamespaceAndPath(
            ModMetadata.MOD_ID,
            "stats_overlay"
    );

    private final @NotNull HorseStatsMod horseStatsMod;

    public HorseStatsModForge(FMLJavaModLoadingContext context) {
        TheModConfig.register(context.getModBusGroup());
        context.registerConfig(ModConfig.Type.CLIENT, TheModConfig.CLIENT_SPEC);
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(HorseStatsConfigScreen::new)
        );

        this.horseStatsMod = new HorseStatsMod(TheModConfig.config);
        PlayerInteractEvent.EntityInteractSpecific.BUS.addListener(this::onEntityInteractEvent);
        ContainerScreenEvent.Render.Foreground.BUS.addListener(this::onRenderContainerScreenEvent);
        InputEvent.InteractionKeyMappingTriggered.PickBlock.BUS.addListener(this::onPickBlockKeyMappingTriggered);
        TickEvent.ClientTickEvent.Post.BUS.addListener(this::onClientTick);
        AddGuiOverlayLayersEvent.BUS.addListener(this::onRegisterGuiLayers);
    }

    private void onRegisterGuiLayers(AddGuiOverlayLayersEvent event) {
        event.getLayeredDraw().addAbove(
                ForgeLayeredDraw.POST_SLEEP_STACK,
                STATS_OVERLAY,
                ForgeLayeredDraw.HOTBAR_MESSAGE,
                horseStatsMod::onRenderOverlayEvent
        );
    }

    private void onPickBlockKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered.PickBlock event) {
        horseStatsMod.onMiddleClickEvent(Minecraft.getInstance());
    }

    private void onClientTick(TickEvent.ClientTickEvent.Post event) {
        horseStatsMod.onClientTick();
    }

    private void onRenderContainerScreenEvent(ContainerScreenEvent.Render.Foreground event) {
        if (event.getContainerScreen() instanceof HorseInventoryScreen horseInventoryScreen) {
            horseStatsMod.onRenderHorseContainerEvent(
                    horseInventoryScreen,
                    event.getGuiGraphics(),
                    event.getMouseX(),
                    event.getMouseY()
            );
        }
    }

    private void onEntityInteractEvent(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getLevel().isClientSide()
                && event.getHand() == InteractionHand.MAIN_HAND
                && event.getTarget() instanceof AbstractHorse horse) {
            horseStatsMod.onHorseInteractEvent(event.getEntity(), horse);
        }
    }
}
