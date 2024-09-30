package dev.gallon.fabric;

import dev.gallon.HorseStatsMod;
import dev.gallon.domain.ModConfig;
import dev.gallon.mixins.AbstractContainerScreenAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public final class HorseStatsModFabric implements ClientModInitializer {
    private final HorseStatsMod horseStatsMod;

    public HorseStatsModFabric() {
        this.horseStatsMod = new HorseStatsMod(new ModConfig());
    }

    @Override
    public void onInitializeClient() {
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
