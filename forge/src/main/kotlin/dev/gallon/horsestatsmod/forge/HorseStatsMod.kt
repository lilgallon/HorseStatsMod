package dev.gallon.horsestatsmod.forge

import dev.gallon.horsestatsmod.common.ModMetadata
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.animal.horse.AbstractHorse
import net.minecraftforge.client.event.ContainerScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.Mod

@Mod(ModMetadata.ID)
class HorseStatsMod {

    init {
        MinecraftForge.EVENT_BUS.addListener(this::onEntityInteractEvent)
        MinecraftForge.EVENT_BUS.addListener(this::onDrawForegroundEvent)
        MinecraftForge.EVENT_BUS.addListener(this::onRenderTickEvent)
    }

    private fun onRenderTickEvent(event: TickEvent.RenderTickEvent) {

    }

    private fun onEntityInteractEvent(event: PlayerInteractEvent.EntityInteractSpecific) {
        val target = event.target
        if (target is AbstractHorse) {
            val stats = target.getStats()

            Minecraft.getInstance()
                .gui
                .setOverlayMessage(
                    Component.translatable("horsestatsmod.health"),
                    false
                )
        }
    }

    private fun onDrawForegroundEvent(event: ContainerScreenEvent.Render.Foreground) {

    }
}
