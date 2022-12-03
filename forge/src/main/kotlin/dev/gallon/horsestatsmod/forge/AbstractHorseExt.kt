package dev.gallon.horsestatsmod.forge

import dev.gallon.horsestatsmod.common.HorseStats
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.horse.AbstractHorse
import net.minecraft.world.entity.animal.horse.Llama
import net.minecraftforge.common.UsernameCache
import kotlin.math.pow

fun AbstractHorse.getStats() = HorseStats(
    health = getAttribute(Attributes.MAX_HEALTH)?.value ?: 0.0,
    jump = getAttribute(Attributes.JUMP_STRENGTH)
        ?.value
        ?.let { rawJump ->
            // Convert to blocks
            - 0.1817584952 * rawJump.pow(3) +
                    3.689713992 * rawJump.pow(2) +
                    2.128599134 * rawJump - 0.343930367
        }
        ?: 0.0,
    speed = getAttribute(Attributes.MOVEMENT_SPEED)
        ?.value
        ?.let { rawSpeed ->
            // convert to m/s
            rawSpeed * 43
        }
        ?: 0.0,
    inventory = when(this) {
        is Llama -> inventoryColumns * 3
        else -> null
    },
    owner = ownerUUID?.run(UsernameCache::getLastKnownUsername)
)
