package dev.gallon.neoforge;

import dev.gallon.HorseStatsMod;
import dev.gallon.domain.ModMetadata;
import dev.gallon.neoforge.config.TheModConfig;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

@Mod(ModMetadata.MOD_ID)
public final class HorseStatsModNeoForge {
    private final @NotNull HorseStatsMod horseStatsMod;

    public HorseStatsModNeoForge(ModContainer container) {
        // Run our common setup.
        this.horseStatsMod = new HorseStatsMod(TheModConfig.config);

        // Make sure the mod being absent on the other network side does not cause the client to display
        // the server as incompatible
        // was needed on forge, maybe not on neoforge
//        ModLoadingContext.get().registerExtensionPoint(
//                IExtensionPoint.DisplayTest.class,
//                () -> new IExtensionPoint.DisplayTest(() -> IGNORESERVERONLY, (remote, isServer) -> true)
//        );

        NeoForge.EVENT_BUS.addListener(this::onEntityInteractEvent);
        NeoForge.EVENT_BUS.addListener(this::onRenderContainerScreenEvent);
        container.registerConfig(ModConfig.Type.CLIENT, TheModConfig.CLIENT_SPEC);
    }

    private void onRenderContainerScreenEvent(ContainerScreenEvent.Render.Foreground event) {
        if (event.getContainerScreen() instanceof HorseInventoryScreen) {
            horseStatsMod.onRenderHorseContainerEvent(
                    (HorseInventoryScreen) event.getContainerScreen(),
                    event.getGuiGraphics(),
                    event.getMouseX(),
                    event.getMouseY()
            );
        }
    }

    private void onEntityInteractEvent(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof AbstractHorse) {
            horseStatsMod.onHorseInteractEvent((AbstractHorse) event.getTarget());
        }
    }
}
