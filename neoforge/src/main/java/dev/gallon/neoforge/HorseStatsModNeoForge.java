package dev.gallon.neoforge;

import dev.gallon.HorseStatsMod;
import dev.gallon.domain.ModMetadata;
import dev.gallon.neoforge.config.TheModConfig;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

@Mod(value = ModMetadata.MOD_ID, dist = Dist.CLIENT)
public final class HorseStatsModNeoForge {
    private final @NotNull HorseStatsMod horseStatsMod;

    public HorseStatsModNeoForge(ModContainer container) {
        this.horseStatsMod = new HorseStatsMod(TheModConfig.config);
        NeoForge.EVENT_BUS.addListener(this::onEntityInteractEvent);
        NeoForge.EVENT_BUS.addListener(this::onRenderContainerScreenEvent);
        container.registerConfig(ModConfig.Type.CLIENT, TheModConfig.CLIENT_SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
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
        if (event.getLevel().isClientSide()
                && event.getHand() == InteractionHand.MAIN_HAND
                && event.getTarget() instanceof AbstractHorse horse) {

            horseStatsMod.onHorseInteractEvent(event.getEntity(), horse);
        }
    }
}
