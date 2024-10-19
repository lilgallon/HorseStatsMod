package dev.gallon.fabric;

import dev.gallon.HorseStatsMod;
import dev.gallon.domain.ModConfig;
import dev.gallon.fabric.config.TheModConfig;
import dev.gallon.mixins.AbstractContainerScreenAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public final class HorseStatsModFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoConfig.register(TheModConfig.class, JanksonConfigSerializer::new);
        ModConfig config = AutoConfig.getConfigHolder(TheModConfig.class).get().modConfig;
        HorseStatsMod horseStatsMod = new HorseStatsMod(config);

        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof HorseInventoryScreen) {
                ScreenEvents.afterRender(screen).register((horseScreen, guiGraphics, mouseX, mouseY, tickDelta) -> {
                    guiGraphics.pose().translate(
                            ((AbstractContainerScreenAccessor) horseScreen).getLeftPos(),
                            ((AbstractContainerScreenAccessor) horseScreen).getTopPos(),
                            0
                    );

                    horseStatsMod.onRenderHorseContainerEvent(
                            (HorseInventoryScreen) horseScreen,
                            guiGraphics,
                            mouseX,
                            mouseY
                    );

                    guiGraphics.pose().translate(
                            -((AbstractContainerScreenAccessor) horseScreen).getLeftPos(),
                            -((AbstractContainerScreenAccessor) horseScreen).getTopPos(),
                            0
                    );
                });
            }
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (hitResult != null && hitResult.getEntity() instanceof AbstractHorse) {
                horseStatsMod.onHorseInteractEvent((AbstractHorse) hitResult.getEntity());
            }
            return InteractionResult.PASS;
        });
    }
}
